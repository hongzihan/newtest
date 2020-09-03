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
    if lualib:MailEx(sender_name, receiver_name, text, gold, yuanbao, integral, templates) then
        return 0
    else
        return cur_action -- 其实也可能因为templates格式错误导致冗余
    end
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
    lualib:AddTimer("", 20190821, 30 * 1000, -1, "CRUD_CHECK_TIMER")
end