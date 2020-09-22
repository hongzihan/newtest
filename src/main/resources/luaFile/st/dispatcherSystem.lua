--|*-------------------------------------------------------|
--|*   通用方法区域  created by ken         BEGIN            |
--|*-------------------------------------------------------|
_G.hzhlib = {}
function no_useful_post_return() end
-- @Usage: 用于向服务端发送专属神途请求
-- @Time: 2020/9/5 14:40
-- @Author: Ken
-- @Param: postData 需要满足 格式 json 且json内需包含actionType&操作类型和actionData&操作数据
-- @Return: void
function hzhlib:postData(postData)
    -- 检测传入类型
    if not (type(postData) == "table" and postData.actionType ~= nil and postData.actionData ~= nil) then
        return
    end
    -- 插入区名
    postData.actionData.zoneName = lualib:GetZoneName()
    -- 将传入的postData数据json化
    local postData_json = json.encode(postData)
    local url = "http://120.78.216.226:8080/openSt/main"
    local data = lualib:GBKToUTF8("jsonMsg="..postData_json)

    lualib:PostURL(url, data, "no_useful_post_return", "", 606)
end
--|*-------------------------------------------------------|
--|*   通用方法区域  created by ken         END              |
--|*-------------------------------------------------------|


--|*-------------------------------------------------------|
--|*   分发管理系统区域  created by ken         BEGIN         |
--|*-------------------------------------------------------|

function action_item_add(action_data, cur_action) -- 给角色增加物品操作函数 -- T==>1<==T
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then -- 玩家在线
        local maxStack = lualib:Item_GetStack(action_data.keyname)
        local mailCount = math.floor(action_data.count / maxStack)
        local extraCount = action_data.count % maxStack
        if mailCount == 0 and extraCount > 0 then
            lualib:Mail("系统", action_data.playername, "", 0, 0, {action_data.keyname, extraCount, 1}  )
        elseif mailCount > 0 and extraCount == 0 then
            for j=1, mailCount do
                lualib:Mail("系统", action_data.playername, "", 0, 0, {action_data.keyname, maxStack, 1}  )
            end
        elseif mailCount > 0 and extraCount > 0 then
            for j=1, mailCount do
                lualib:Mail("系统", action_data.playername, "", 0, 0, {action_data.keyname, maxStack, 1}  )
            end
            lualib:Mail("系统", action_data.playername, "", 0, 0, {action_data.keyname, extraCount, 1}  )
        end
        return 0
    else
        return cur_action
    end
end

function action_item_del(action_data, cur_action) -- 删除角色物品操作函数 -- T==>2<==T
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        if lualib:DelItem(playerGUID, action_data.keyname, action_data.count, 2, "web分发删除", "web分发删除") then
            return 0
        else
            return cur_action
        end
    else
        return cur_action
    end
end

function action_var_system_str(action_data, cur_action) -- 设置系统str变量 -- T==>3<==T
    local strKey = action_data.varname
    local dbValue = action_data.varvalue
    local byType = action_data.areatype
    if strKey == "" or dbValue == "" or type(strKey) ~= "string" or type(dbValue) ~= "string" or type(byType) ~= "number" then
        return 0 -- 如果类型错误，也得删了，不然会造成数据冗余，永远无法处理
    end
    if byType <= 0 or byType >= 7 then -- 默认为6
        lualib:SetDBStrEx(strKey, dbValue, 6)
    else
        lualib:SetDBStrEx(strKey, dbValue, byType)
    end
    return 0
end

function action_var_system_int(action_data, cur_action) -- 设置系统num变量 -- T==>4<==T
    local strKey = action_data.varname
    local dbValue = tonumber(action_data.varvalue)
    local byType = action_data.areatype

    if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "number" or type(byType) ~= "number" then
        return 0 -- 如果类型错误，也得删了，不然会造成数据冗余，永远无法处理
    end

    if byType <= 0 or byType >= 7 then -- 默认为6
        lualib:SetDBNumEx(strKey, dbValue, 6)
    else
        lualib:SetDBNumEx(strKey, dbValue, byType)
    end
    return 0
end

function action_var_player_str(action_data, cur_action) -- 设置玩家str变量 -- T==>5<==T
    if action_data.playername == "" then
        return 0
    end
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        local strKey = action_data.varname
        local dbValue = action_data.varvalue
        if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "string" then
            return 0 -- 如果类型错误，也得删了，不然会造成数据冗余，永远无法处理
        end
        lualib:SetStr(playerGUID, strKey, dbValue)
        return 0
    else
        return cur_action
    end
end

function action_var_player_int(action_data, cur_action) -- 设置玩家int变量 -- T==>6<==T
    if action_data.playername == "" then
        return 0
    end
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        local strKey = action_data.varname
        local dbValue = tonumber(action_data.varvalue)
        if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "number" then
            return 0 -- 如果类型错误，也得删了，不然会造成数据冗余，永远无法处理
        end
        lualib:SetInt(playerGUID, strKey, dbValue)
        return 0
    else
        return cur_action
    end
end

function action_mail_ex(action_data, cur_action) -- 按照数据要求给玩家发邮件 -- T==>7<==T
    local sender_name = action_data.title
    local receiver_name = action_data.playername
    local text = action_data.text -- 可以为""
    local gold = action_data.gold
    local yuanbao = action_data.yuanbao
    local integral = action_data.integral
    local templates = action_data.templates -- 可以为{}

    local status,err = pcall(function ()
        templates = json.decode(templates)
        return "success"
    end)

    if not status then
        return 0 -- 错了也要删
    end

    if sender_name == "" or receiver_name == "" then
        return 0
    end
    if type(sender_name) ~= "string" or type(receiver_name) ~= "string" or type(text) ~= "string" or type(gold) ~= "number" then
        return 0
    end
    if type(yuanbao) ~= "number" or type(integral) ~= "number" or type(templates) ~= "table" then
        return 0
    end
    local status,err = pcall(function ()
        lualib:MailEx(sender_name, receiver_name, text, gold, yuanbao, integral, templates)
    end)

    return 0
end

function action_give_currency(action_data, cur_action) -- 按照数据要求给玩家发一般物品如金币，经验，元宝，积分 -- T==>8<==T
    local username = action_data.username
    local num = action_data.num
    local giveType = action_data.type
    local playerGUID = lualib:Name2Guid(username)
    if playerGUID ~= "" then
        if giveType == "非绑金币" then
            lualib:AddGold(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "绑定金币" then
            lualib:AddBindGold(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "非绑元宝" then
            lualib:Player_AddIngot(playerGUID, tonumber(num), false, "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "绑定元宝" then
            lualib:Player_AddIngot(playerGUID, tonumber(num), true, "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "经验" then
            lualib:AddExp(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "积分" then
            lualib:AddIntegral(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        end
    else
        return cur_action
    end
    return 0
end

function action_charge_monitor(action_data, cur_action) -- 按照数据要求给玩家进行模拟充值 -- T==>9<==T
    local username = action_data.username
    local num = action_data.num
    local chargePercent = action_data.chargePercent
    local playerGUID = lualib:Name2Guid(username)
    if playerGUID ~= "" then
        local user_id = lualib:UserID(playerGUID)
        local function monitor_charge_child(yb)
            local status,err = pcall(function ()
                on_trigger_billin(playerGUID, yb, "another")
                return "success"
            end)
            local status2,err2 = pcall(function ()
                on_billinex(user_id, yb)
                return "success"
            end)
            if status or status2 then
                lualib:AddIngot(playerGUID, yb, "web_action", "web_action")
                lualib:SetDBNum("define_bill"..user_id,lualib:GetDBNum("define_bill"..user_id)+yb)
                return true
            else
                return false
            end
        end
        -- 由于网络波动会导致充值和返利顺序不同，所以在这里模拟一下
        local suc = false
        local yb = tonumber(num) * 100
        local welfareYb = yb * chargePercent / 100
        if math.random(0, 100) >= 50 then
            suc = monitor_charge_child(yb)
            if chargePercent > 0 then
                suc = monitor_charge_child(welfareYb)
            end
        else
            if chargePercent > 0 then
                suc = monitor_charge_child(welfareYb)
            end
            suc = monitor_charge_child(yb)
        end
        -- 最后记得判断是否充值完毕
        if suc then
            return 0
        else
            return 0
        end
    else
        return 0
    end
    return 0
end

function action_give_currency(action_data, cur_action) -- 按照数据要求给玩家发一般物品如金币，经验，元宝，积分 -- T==>9<==T
    local username = action_data.username
    local num = action_data.num
    local giveType = action_data.type
    local playerGUID = lualib:Name2Guid(username)
    if playerGUID ~= "" then
        if giveType == "非绑金币" then
            lualib:AddGold(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "绑定金币" then
            lualib:AddBindGold(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "非绑元宝" then
            lualib:Player_AddIngot(playerGUID, tonumber(num), false, "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "绑定元宝" then
            lualib:Player_AddIngot(playerGUID, tonumber(num), true, "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "经验" then
            lualib:AddExp(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        elseif giveType == "积分" then
            lualib:AddIntegral(playerGUID, tonumber(num), "web_action_give"..giveType, "web_action_give"..giveType)
            lualib:SysWarnMsg(playerGUID, "获得"..giveType..tostring(num))
            return 0
        end
    else
        return cur_action
    end
    return 0
end

function action_monster_refresh_kill(action_data, cur_action) -- 按照数据要求来刷新或杀死怪物 -- T==>10<==T
    local mobKey = action_data.mobKey
    local mapKey = action_data.mapKey
    local x = action_data.coordinateX
    local y = action_data.coordinateY
    local range = action_data.range
    local num = action_data.num
    local cType = action_data.cType
    if cType == 1 then --刷怪
        if x ~= 0 and y ~= 0 then
            pcall(function()
                lualib:Map_GenMonster(lualib:Map_GetMapGuid(mapKey), x, y, range, math.random(1,4), mobKey, num, false)
            end)
            return 0
        else
            pcall(function()
                lualib:Map_BatchGenCampMonster(lualib:Map_GetMapGuid(mapKey), mobKey, num, false, 2)
            end)
            return 0
        end
    elseif cType == 2 then-- 杀怪
        pcall(function ()
            local mobList = lualib:Map_GetRegionMonstersEx(lualib:Map_GetMapGuid(mapKey), mobKey, {1,0,0,1000,1000}, true, true)
            local countNum = 0
            for _,v in pairs(mobList) do
                if countNum < num then
                    lualib:Kill(v)
                else
                    break
                end
                countNum = countNum + 1
            end
        end)
        return 0
    elseif cType == 3 then
        pcall(function ()
            if (x ~= 0 and y ~= 0) then
                lualib:Map_GenNpc(lualib:Map_GetMapGuid(mapKey), mobKey, x, y, 0, num)
            end
        end)
        return 0
    elseif cType == 4 then
        pcall(function ()
            if (x ~= 0 and y ~= 0) then
                local mob_list = lualib:Map_GetRegionMonstersEx(lualib:Map_GetMapGuid(mapKey), mobKey, {0, x, y, range, range}, true, true)
                local countNum = 0
                for _,v in pairs(mob_list) do
                    if countNum < num then
                        lualib:Kill(v)
                    else
                        break
                    end
                    countNum = countNum + 1
                end
            end
        end)
    else
        return 0
    end
        return 0
end

function action_send_message(action_data, cur_action) -- 按照数据要求来发送不同类型的消息 -- T==>11<==T
    local msgType = action_data.msgType
    local content = action_data.content
    local username = action_data.username
    local foreground = action_data.foreground
    local background = action_data.background
    local playerGUID = ""
    if username ~= "" then
        playerGUID = lualib:Name2Guid(username)
        if playerGUID == "" then
            return cur_action
        end
    end
    if msgType == 1 then
        lualib:SysMsg_SendBroadcastMsg(tostring(content), "系统")
        return 0
    elseif msgType == 2 then
        lualib:SysWarnMsg(playerGUID, content)
        return 0
    elseif msgType == 3 then
        lualib:MsgBox(playerGUID, content)
        return 0
    elseif msgType == 4 then
        lualib:SysMsg_SendBroadcastColor(tostring(content), "系统", foreground, background)
        return 0
    end
    return 0
end

function action_copy_role_item_to_other(action_data, cur_action) -- 按照数据要求复制某玩家物品到目标玩家包裹 -- T==>12<==T
    local originUsername = action_data.originUsername
    local targetUsername = action_data.targetUsername
    local originPlayer ,targetPlayer= "", ""
    if originUsername ~= "" and targetUsername ~= "" then
        originPlayer = lualib:Name2Guid(originUsername)
        targetPlayer = lualib:Name2Guid(targetUsername)
        if originPlayer == "" or targetPlayer == "" then
            return cur_action
        end
    end
    local equipWine = {}
    for i=1, 40 do
        local originEquip = lualib:Player_GetItemGuid(originPlayer, i)
        if originEquip ~= "" then
            if lualib:Item_GetType("", originEquip) == 1 then
                table.insert(equipWine,1, lualib:Item2Json(originEquip))
            end
        end
    end
    if lualib:GetBagFree(targetPlayer) < #equipWine then
        return cur_action
    end
    for i=1,#equipWine do
        lualib:Json2ItemEx(targetPlayer, equipWine[i],true)
    end

    return 0
end

function action_copy_role_var_to_other_destroy(action_data, cur_action) -- 按照数据要求将某个玩家的变量转移到另一个玩家身上并销毁 -- T==>13<==T
    local usernameBefore,accountBefore,idBefore = action_data.usernameBefore,action_data.accountBefore,action_data.idBefore
    local usernameAfter,accountAfter,idAfter = action_data.usernameAfter,action_data.accountAfter,action_data.idAfter
    local accountSerialize = action_data.accountSerialize
    -- 解析账户序列
    local accountWineTable = lualib:StrSplit(accountSerialize, "&")
    for i=1,#accountWineTable do
        local accountVarTable = lualib:StrSplit(accountWineTable, "$")
        if accountVarTable[1] == "0" then
            local before_key = string.gsub(accountVarTable[2], "#", accountBefore,1)
            local after_key = string.gsub(accountVarTable[2], "#", accountAfter,1)
            if lualib:GetDBNum(before_key) ~= 0 then
                lualib:SetDBNumEx(after_key, accountVarTable[3], accountVarTable[4])
                lualib:SetDBNumEx(before_key, 0, accountVarTable[4])
            end
        elseif accountVarTable[2] == "1" then
            local before_key = string.gsub(accountVarTable[2], "#", accountBefore,1)
            local after_key = string.gsub(accountVarTable[2], "#", accountAfter,1)
            if lualib:GetDBStr(before_key) ~= "" then
                lualib:SetDBStrEx(after_key, accountVarTable[3], accountVarTable[4])
                lualib:SetDBStrEx(before_key, "", accountVarTable[4])
            end
        end
    end

    local allDBTable = lualib:GetAllDBVars() -- 1.变量类型 2.key 3.value 4.合区类型

    for key,val in pairs(allDBTable) do
        -- 用户名 账户名 用户userid
        -- 用户名 和 账户名 需要防止和变量名重复
        if val[1] == 0 then
            if string.find(val[2], idBefore) ~= nil then -- 变量中确实含有id
                local new_key = string.gsub(val[2], idBefore, idAfter)
                lualib:SetDBNumEx(new_key, val[3], val[4])
                --lualib:SysMsg_SendBroadcastMsg(tostring(new_key).." => "..tostring(val[3]).." 标记>>"..tostring(val[4]), "")
                lualib:SetDBNumEx(val[2], 0, val[4])
                --lualib:SysMsg_SendBroadcastMsg(tostring(val[2]).." => "..tostring(val[3]).." 标记>>"..tostring(val[4]), "")
            end
        elseif val[2] == 1 then
            if string.find(val[2], idBefore) ~= nil then -- 变量中确实含有id
                local new_key = string.gsub(val[2], idBefore, idAfter)
                lualib:SetDBStrEx(new_key, val[3], val[4])
                --lualib:SysMsg_SendBroadcastMsg(tostring(new_key).." => "..tostring(val[3]).." 标记>>"..tostring(val[4]), "")
                lualib:SetDBStrEx(val[2], "", val[4])
                --lualib:SysMsg_SendBroadcastMsg(tostring(val[2]).." => "..tostring(val[3]).." 标记>>"..tostring(val[4]), "")
            end
        end
    end
    return 0
end

function remove_table_value_nil(extra_data) -- 移除目标table内无效值并返回一个新的table
    local extra_data_new = {}
    for i=1, #extra_data do
        if type(extra_data[i]) == "table" then
            table.insert(extra_data_new, 1, extra_data[i])
        end
    end
    return extra_data_new
end

function save_extra_msg(cType, extra_msg_table) -- 保存action的冗余数据
    if cType == "client" then
        if #extra_msg_table <= 0 then
            lualib:SetDBStrEx("crud_action_extra_data", "", 6) -- 重新存入
        else
            lualib:SetDBStrEx("crud_action_extra_data", json.encode(extra_msg_table), 6) -- 重新存入
        end
    else -- 服务器数据处理
        if #extra_msg_table <= 0 then
            return
        end
        local jsonStr = lualib:GetDBStr("crud_action_extra_data")
        local extra_data = {} -- 定义一个空table
        if jsonStr == "" then -- 如果数据库无数据，则赋值第一个数据
            extra_data[1] = extra_msg_table[1]
            table.remove(extra_msg_table, 1)
        else
            extra_data = json.decode(jsonStr)
        end
        -- 接下来是剩余数据处理了
        for i=1, #extra_msg_table do -- 这里不能从2开始，因为数据库一开始可能有数据，所以不一定会发生赋值
            table.insert(extra_data, 1, extra_msg_table[i]) -- 循环添加值
        end
        lualib:SetDBStrEx("crud_action_extra_data", json.encode(extra_data), 6) -- 重新存入
    end
end

function super_old_horse_dispathtcher(extra_data) -- web分发，流水线部分
    for i=1, #extra_data do
        local action_type = extra_data[i].actiontype
        local action_data_json = extra_data[i].actiondata
        if action_data_json ~= "" and action_data_json ~= "{}" and action_data_json ~= nil then -- json正常解析后进行操作分派，流水线
            local action_data = json.decode(action_data_json)
            if action_type == 1 then -- 物品增加操作
                extra_data[i] = action_item_add(action_data, extra_data[i])
            elseif action_type == 2 then -- 物品删除操作
                extra_data[i] = action_item_del(action_data, extra_data[i])
            elseif action_type == 3 then
                extra_data[i] = action_var_system_str(action_data, extra_data[i])
            elseif action_type == 4 then
                extra_data[i] = action_var_system_int(action_data, extra_data[i])
            elseif action_type == 5 then
                extra_data[i] = action_var_player_str(action_data, extra_data[i])
            elseif action_type == 6 then
                extra_data[i] = action_var_player_int(action_data, extra_data[i])
            elseif action_type == 7 then
                extra_data[i] = action_mail_ex(action_data, extra_data[i])
            elseif action_type == 8 then
                extra_data[i] = action_give_currency(action_data, extra_data[i])
            elseif action_type == 9 then
                extra_data[i] = action_charge_monitor(action_data, extra_data[i])
            elseif action_type == 10 then
                extra_data[i] = action_monster_refresh_kill(action_data, extra_data[i])
            elseif action_type == 11 then
                extra_data[i] = action_send_message(action_data, extra_data[i])
            elseif action_type == 12 then
                extra_data[i] = action_copy_role_item_to_other(action_data, extra_data[i])
            elseif action_type == 13 then
                extra_data[i] = action_copy_role_var_to_other_destroy(action_data, extra_data[i])
            end
        else -- json数据异常，直接移除，防止系统出错
            extra_data[i] = 0
        end
    end
    return extra_data
end

function super_old_horse_dispathtcher_main(cType, content) -- web分发主体
    local json_data = content
    if json_data ~= "" and json_data ~= "{}" and json_data ~= nil then -- 数据验证
        local item_action_data = json.decode(json_data) -- 数据解析
        item_action_data = super_old_horse_dispathtcher(item_action_data) -- 数据操作
        local newTable = remove_table_value_nil(item_action_data) -- 移除空数据
        save_extra_msg(cType, newTable) -- 保留多余数据进入冗余池
    end
end

function web_back_crud_action(a, b, content) -- 网络返回回调
    local status,err = pcall(function ()
        xx = json.decode(lualib:UTF8ToGBK(content))
        return "success"
    end)

    if not status then
        return
    end
    super_old_horse_dispathtcher_main("server", lualib:UTF8ToGBK(content)) -- 处理来自服务端的数据
    super_old_horse_dispathtcher_main("client", lualib:GetDBStr("crud_action_extra_data")) -- 处理来自游戏本身的因玩家离线造成的数据冗余
end

function CRUD_CHECK_TIMER()
    local url = "http://120.78.216.226:8080/webAction/getActionData"
    local zoneid = lualib:GetZoneName()
    local data = "zoneid="..tostring(zoneid)
    lualib:PostURL(url, lualib:GBKToUTF8(data), "web_back_crud_action", "", 800)
end

function lualib:dispathcher_main()
    -- 主函数
    local url = "http://120.78.216.226:8080/config/insertZoneNameConfig"
    local data = "zoneName="..tostring(lualib:GetZoneName())
    lualib:PostURL(url, lualib:GBKToUTF8(data), "no_useful_post_return", "", 800)
    lualib:AddTimer("0", 20190821, 30 * 1000, -1, "CRUD_CHECK_TIMER") -- 操作数据分发
    lualib:AddTimerEx("0", 90162, 60 * 1000, -1, "dispatcherData_disjava", "") -- 掉落数据分发
end

--|*-------------------------------------------------------|
--|*   分发管理系统区域  created by ken         END           |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   物品掉落统计区域  created by ken         BEGIN         |
--|*-------------------------------------------------------|
local DROP_TABLE_DISJAVA_NUM = 300 -- 掉落计数总表数
local DROP_TABLE_DISJAVA_MAX_NUM = 50 -- 每个表最大存储数量
function saveDropData_disjava(item) -- 主函数，外界只需要在require该脚本后调用该函数即可
    if 1 == 1 then
        return -- 暂时关闭掉落统计
    end
    local indexstr = lualib:GetDBStr("drop_item_all_index")
    local index_table = {}
    if indexstr == "" then
        local allIndex = {}
        for i=1, DROP_TABLE_DISJAVA_NUM do
            allIndex[i] = 0
        end
        lualib:SetDBStrEx("drop_item_all_index", json.encode(allIndex), 6)
        index_table = json.decode(json.encode(allIndex))
    else
        index_table = json.decode(indexstr)
    end

    for i=1, DROP_TABLE_DISJAVA_NUM do
        local isfull = lualib:GetDBStr("drop_item_table_dispatcher_isfull"..tostring(i))
        if isfull ~= "true" then -- 如果该表还没满，那就可以继续操作咯
            index_table[i] = 1
            lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
            local str = lualib:GetDBStr("drop_item_table_dispatcher_"..tostring(i))
            if str == "" then -- 如果表是空的，则建新表，并存入第一个数据
                local drop_item = {}
                drop_item[1] = {keyname = lualib:KeyName(item), itemname = lualib:Name(item), count = 1}
                lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), json.encode(drop_item), 6)
                break
            else
                local drop_item = json.decode(str)
                local item_length = #drop_item
                if item_length + 1 < DROP_TABLE_DISJAVA_MAX_NUM and item_length + 1 >= 1 then -- 如果表还没存满
                    local isexist = 0
                    local item_keyname = lualib:KeyName(item)
                    for i=1, item_length do
                        if drop_item[i].keyname == item_keyname then
                            isexist = i
                            break
                        end
                    end
                    if isexist == 0 then
                        drop_item[item_length + 1] = {keyname = lualib:KeyName(item), itemname = lualib:Name(item), count = 1}
                    else
                        if drop_item[isexist] ~= nil then
                            drop_item[isexist].count = drop_item[isexist].count + 1
                        end
                    end

                    lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), json.encode(drop_item), 6)
                    break
                else -- 数据表在存入这一个以后就达到了最大
                    drop_item[item_length + 1] = {keyname = lualib:KeyName(item), itemname = lualib:Name(item), count = 1}
                    lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), json.encode(drop_item), 6)
                    lualib:SetDBStrEx("drop_item_table_dispatcher_isfull"..tostring(i), "true", 6)
                    break
                end
            end
        end
    end
end


function dispatcherData_disjava()
    local indexstr = lualib:GetDBStr("drop_item_all_index")
    local itrNum = 0
    local index_table = {}
    if indexstr == "" then
        local allIndex = {}
        for i=1, DROP_TABLE_DISJAVA_NUM do
            allIndex[i] = 0
        end
        lualib:SetDBStrEx("drop_item_all_index", json.encode(allIndex), 6)
        index_table = json.decode(json.encode(allIndex))
    else
        index_table = json.decode(indexstr)
        for i=1, #index_table do
            if index_table[i] == 1 then
                itrNum = i
            end
        end
    end
    for i=1, itrNum do

        local isfull = lualib:GetDBStr("drop_item_table_dispatcher_isfull"..tostring(i))
        local str = lualib:GetDBStr("drop_item_table_dispatcher_"..tostring(i))
        if isfull == "true" then -- 如果表满了就可以开始post了
            if not lualib:HasTimer("0", 90163) then
                lualib:SetDBStrEx("now_dispatcher_data_java", str, 6)
                lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), "", 6)
                lualib:SetDBStrEx("drop_item_table_dispatcher_isfull"..tostring(i), "", 6) -- 只要不是true就行
                lualib:AddTimerEx("0", 90163, 333, DROP_TABLE_DISJAVA_MAX_NUM, "dispatcher_real_sender_disjava", "")
                index_table[i] = 0
                lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
                break
            else
                -- 有数据在分发了，先不打扰了
                break
            end
        elseif i > 1 then
            if lualib:GetDBStr("drop_item_table_dispatcher_isfull" .. tostring(i - 1)) ~= "true" then
                if str ~= "" and str ~= "{}" then
                    local drop_item = json.decode(str)
                    if not lualib:HasTimer("0", 90163) then
                        lualib:SetDBStrEx("now_dispatcher_data_java", str, 6)
                        lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), "", 6)
                        lualib:SetDBStrEx("drop_item_table_dispatcher_isfull"..tostring(i), "", 6) -- 只要不是true就行
                        lualib:AddTimerEx("0", 90163, 333, #drop_item, "dispatcher_real_sender_disjava", "")
                        index_table[i] = 0
                        lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
                        break
                    else
                        -- 有数据在分发了，先不打扰了
                        break
                    end
                end
            end

        end
    end
end


function dispatcher_real_sender_disjava()
    local str = lualib:GetDBStr("now_dispatcher_data_java")
    if str ~= "" then
        local drop_item = json.decode(str)
        local url = "http://120.78.216.226:8080/dropItem/insertNewData"
        local data = "keyname="..tostring(drop_item[1].keyname).."&itemname="..tostring(drop_item[1].itemname).."&count="..tostring(drop_item[1].count).."&zoneid="..tostring(lualib:GetZoneName())
        lualib:PostURL(url, lualib:GBKToUTF8(data), "web_back_useitem", "", 500)
        table.remove(drop_item, 1)
        if #drop_item == 0 then
            lualib:SetDBStrEx("now_dispatcher_data_java", "", 6)
        else
            lualib:SetDBStrEx("now_dispatcher_data_java", json.encode(drop_item), 6)
        end
    end

end

--|*-------------------------------------------------------|
--|*   物品掉落统计区域  created by ken      END              |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   GS BOSS 击杀统计 created by ken       BEGIN          |
--|*-------------------------------------------------------|

function addRecordToServer(monster, killer)
    if monster == "" or killer == "" or monster == nil or killer == nil then
        return
    end
    local keyname = lualib:KeyName(monster)
    if lualib:Monster_Type(keyname) ~= 4 then
        return
    end
    if not lualib:Player_IsPlayer(killer) then
        local killerName = lualib:Monster_GetMaster(killer)
        if killerName ~= "" then
            killer = lualib:Name2Guid(killerName)
        end
    end
    local killername = lualib:Name(killer)
    local mapname = lualib:Name(lualib:MapGuid(killer))
    local dietime = lualib:Time2Str("%Y-%m-%d %H:%M:%S", lualib:GetAllTime())
    local url = "http://120.78.216.226:8080/openSt/insertNewMonster"
    local zonename = lualib:GetZoneName()
    local data = "zonename="..tostring(zonename).."&mobname="..tostring(keyname).."&killer="..tostring(killername)
    data = data.."&mapname="..tostring(mapname).."&dietime="..tostring(dietime)

    lualib:PostURL(url, lualib:GBKToUTF8(data), "web_back_crud_action", "", 800)
end

--|*-------------------------------------------------------|
--|*   GS BOSS 击杀统计  created by ken      END            |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   元宝充值统计 created by ken       BEGIN               |
--|*-------------------------------------------------------|

function insertChargeData(player, yb)
    local postData = {}
    postData.actionData = {}
    postData.actionData.chargeNum = yb
    postData.actionData.accountName = lualib:AccountName(player)
    postData.actionData.username = lualib:Name(player)
    postData.actionType = "插入充值数据"
    hzhlib:postData(postData)
end

--|*-------------------------------------------------------|
--|*   元宝充值统计 created by ken       END                 |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   聊天记录监控 created by ken       BEGIN               |
--|*-------------------------------------------------------|
local chatChannelName = {
    [1] = "当前",
    [2] = "组队",
    [3] = "行会",
    [4] = "世界",
    [5] = "喇叭",
    [7] = "私聊",
}
function insertChatRecordData(player,content,channel)
    if channel == "nil" or channel == nil then
        return
    end
    local postData = {}
    postData.actionData = {}
    postData.actionData.channelName = chatChannelName[tonumber(channel)] == nil and "未知" or chatChannelName[tonumber(channel)]
    postData.actionData.content = content
    postData.actionData.username = lualib:Name(player)
    postData.actionData.dateTime = lualib:Time2Str("%Y-%m-%d %H:%M:%S", lualib:GetAllTime())
    postData.actionType = "插入聊天记录"
    hzhlib:postData(postData)
end
--|*-------------------------------------------------------|
--|*   聊天记录监控 created by ken       END               |
--|*-------------------------------------------------------|