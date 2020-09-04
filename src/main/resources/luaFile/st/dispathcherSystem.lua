--|*-------------------------------------------------------|
--|*   �ַ�����ϵͳ����  created by ken         BEGIN         |
--|*-------------------------------------------------------|

function action_item_add(action_data, cur_action) -- ����ɫ������Ʒ�������� -- T==>1<==T
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then -- �������
        local maxStack = lualib:Item_GetStack(action_data.keyname)
        local mailCount = math.floor(action_data.count / maxStack)
        local extraCount = action_data.count % maxStack
        if mailCount == 0 and extraCount > 0 then
            lualib:Mail("ϵͳ", action_data.playername, "", 0, 0, {action_data.keyname, extraCount, 1}  )
        elseif mailCount > 0 and extraCount == 0 then
            for j=1, mailCount do
                lualib:Mail("ϵͳ", action_data.playername, "", 0, 0, {action_data.keyname, maxStack, 1}  )
            end
        elseif mailCount > 0 and extraCount > 0 then
            for j=1, mailCount do
                lualib:Mail("ϵͳ", action_data.playername, "", 0, 0, {action_data.keyname, maxStack, 1}  )
            end
            lualib:Mail("ϵͳ", action_data.playername, "", 0, 0, {action_data.keyname, extraCount, 1}  )
        end
        return 0
    else
        return cur_action
    end
end

function action_item_del(action_data, cur_action) -- ɾ����ɫ��Ʒ�������� -- T==>2<==T
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        if lualib:DelItem(playerGUID, action_data.keyname, action_data.count, 2, "web�ַ�ɾ��", "web�ַ�ɾ��") then
            return 0
        else
            return cur_action
        end
    else
        return cur_action
    end
end

function action_var_system_str(action_data, cur_action) -- ����ϵͳstr���� -- T==>3<==T
    local strKey = action_data.varname
    local dbValue = action_data.varvalue
    local byType = action_data.areatype
    if strKey == "" or dbValue == "" or type(strKey) ~= "string" or type(dbValue) ~= "string" or type(byType) ~= "number" then
        return 0 -- ������ʹ���Ҳ��ɾ�ˣ���Ȼ������������࣬��Զ�޷�����
    end
    if byType <= 0 or byType >= 7 then -- Ĭ��Ϊ6
        lualib:SetDBStrEx(strKey, dbValue, 6)
    else
        lualib:SetDBStrEx(strKey, dbValue, byType)
    end
    return 0
end

function action_var_system_int(action_data, cur_action) -- ����ϵͳnum���� -- T==>4<==T
    local strKey = action_data.varname
    local dbValue = tonumber(action_data.varvalue)
    local byType = action_data.areatype

    if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "number" or type(byType) ~= "number" then
        return 0 -- ������ʹ���Ҳ��ɾ�ˣ���Ȼ������������࣬��Զ�޷�����
    end

    if byType <= 0 or byType >= 7 then -- Ĭ��Ϊ6
        lualib:SetDBNumEx(strKey, dbValue, 6)
    else
        lualib:SetDBNumEx(strKey, dbValue, byType)
    end
    return 0
end

function action_var_player_str(action_data, cur_action) -- �������str���� -- T==>5<==T
    if action_data.playername == "" then
        return 0
    end
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        local strKey = action_data.varname
        local dbValue = action_data.varvalue
        if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "string" then
            return 0 -- ������ʹ���Ҳ��ɾ�ˣ���Ȼ������������࣬��Զ�޷�����
        end
        lualib:SetStr(playerGUID, strKey, dbValue)
        return 0
    else
        return cur_action
    end
end

function action_var_player_int(action_data, cur_action) -- �������int���� -- T==>6<==T
    if action_data.playername == "" then
        return 0
    end
    local playerGUID = lualib:Name2Guid(action_data.playername)
    if playerGUID ~= "" then
        local strKey = action_data.varname
        local dbValue = tonumber(action_data.varvalue)
        if strKey == "" or type(strKey) ~= "string" or type(dbValue) ~= "number" then
            return 0 -- ������ʹ���Ҳ��ɾ�ˣ���Ȼ������������࣬��Զ�޷�����
        end
        lualib:SetInt(playerGUID, strKey, dbValue)
        return 0
    else
        return cur_action
    end
end

function action_mail_ex(action_data, cur_action) -- ��������Ҫ�����ҷ��ʼ� -- T==>7<==T
    local sender_name = action_data.title
    local receiver_name = action_data.playername
    local text = action_data.text -- ����Ϊ""
    local gold = action_data.gold
    local yuanbao = action_data.yuanbao
    local integral = action_data.integral
    local templates = action_data.templates -- ����Ϊ{}

    local status,err = pcall(function ()
        templates = json.decode(templates)
        return "success"
    end)

    if not status then
        return 0 -- ����ҲҪɾ
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
        return cur_action -- ��ʵҲ������Ϊtemplates��ʽ����������
    end
end

function remove_table_value_nil(extra_data) -- �Ƴ�Ŀ��table����Чֵ������һ���µ�table
    local extra_data_new = {}
    for i=1, #extra_data do
        if type(extra_data[i]) == "table" then
            table.insert(extra_data_new, 1, extra_data[i])
        end
    end
    return extra_data_new
end

function save_extra_msg(cType, extra_msg_table) -- ����action����������
    if cType == "client" then
        if #extra_msg_table <= 0 then
            lualib:SetDBStrEx("crud_action_extra_data", "", 6) -- ���´���
        else
            lualib:SetDBStrEx("crud_action_extra_data", json.encode(extra_msg_table), 6) -- ���´���
        end
    else -- ���������ݴ���
        if #extra_msg_table <= 0 then
            return
        end
        local jsonStr = lualib:GetDBStr("crud_action_extra_data")
        local extra_data = {} -- ����һ����table
        if jsonStr == "" then -- ������ݿ������ݣ���ֵ��һ������
            extra_data[1] = extra_msg_table[1]
            table.remove(extra_msg_table, 1)
        else
            extra_data = json.decode(jsonStr)
        end
        -- ��������ʣ�����ݴ�����
        for i=1, #extra_msg_table do -- ���ﲻ�ܴ�2��ʼ����Ϊ���ݿ�һ��ʼ���������ݣ����Բ�һ���ᷢ����ֵ
            table.insert(extra_data, 1, extra_msg_table[i]) -- ѭ�����ֵ
        end
        lualib:SetDBStrEx("crud_action_extra_data", json.encode(extra_data), 6) -- ���´���
    end
end

function super_old_horse_dispathtcher(extra_data) -- web�ַ�����ˮ�߲���
    for i=1, #extra_data do
        local action_type = extra_data[i].actiontype
        local action_data_json = extra_data[i].actiondata
        if action_data_json ~= "" and action_data_json ~= "{}" and action_data_json ~= nil then -- json������������в������ɣ���ˮ��
            local action_data = json.decode(action_data_json)
            if action_type == 1 then -- ��Ʒ���Ӳ���
                extra_data[i] = action_item_add(action_data, extra_data[i])
            elseif action_type == 2 then -- ��Ʒɾ������
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
        else -- json�����쳣��ֱ���Ƴ�����ֹϵͳ����
            extra_data[i] = 0
        end
    end
    return extra_data
end

function super_old_horse_dispathtcher_main(cType, content) -- web�ַ�����
    local json_data = content
    if json_data ~= "" and json_data ~= "{}" and json_data ~= nil then -- ������֤
        local item_action_data = json.decode(json_data) -- ���ݽ���
        item_action_data = super_old_horse_dispathtcher(item_action_data) -- ���ݲ���
        local newTable = remove_table_value_nil(item_action_data) -- �Ƴ�������
        save_extra_msg(cType, newTable) -- �����������ݽ��������
    end
end

function web_back_crud_action(a, b, content) -- ���緵�ػص�
    local status,err = pcall(function ()
        xx = json.decode(lualib:UTF8ToGBK(content))
        return "success"
    end)

    if not status then
        return
    end
    super_old_horse_dispathtcher_main("server", lualib:UTF8ToGBK(content)) -- �������Է���˵�����
    super_old_horse_dispathtcher_main("client", lualib:GetDBStr("crud_action_extra_data")) -- ����������Ϸ����������������ɵ���������
end

function CRUD_CHECK_TIMER()
    local url = "http://120.78.216.226:8080/webAction/getActionData"
    local zoneid = lualib:GetZoneName()
    local data = "zoneid="..tostring(zoneid)
    lualib:PostURL(url, lualib:GBKToUTF8(data), "web_back_crud_action", "", 800)
end

function lualib:dispathcher_main()
    -- ������
    lualib:AddTimer("", 20190821, 30 * 1000, -1, "CRUD_CHECK_TIMER")
end

--|*-------------------------------------------------------|
--|*   �ַ�����ϵͳ����  created by ken         END           |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   ��Ʒ����ͳ������  created by ken         BEGIN         |
--|*-------------------------------------------------------|
local DROP_TABLE_DISJAVA_NUM = 300 -- ��������ܱ���
local DROP_TABLE_DISJAVA_MAX_NUM = 50 -- ÿ�������洢����
function saveDropData_disjava(item) -- �����������ֻ��Ҫ��require�ýű�����øú�������
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
        if isfull ~= "true" then -- ����ñ�û�����ǾͿ��Լ���������
            index_table[i] = 1
            lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
            local str = lualib:GetDBStr("drop_item_table_dispatcher_"..tostring(i))
            if str == "" then -- ������ǿյģ����±��������һ������
                local drop_item = {}
                drop_item[1] = {keyname = lualib:KeyName(item), itemname = lualib:Name(item), count = 1}
                lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), json.encode(drop_item), 6)
                break
            else
                local drop_item = json.decode(str)
                local item_length = #drop_item
                if item_length + 1 < DROP_TABLE_DISJAVA_MAX_NUM and item_length + 1 >= 1 then -- �����û����
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
                else -- ���ݱ��ڴ�����һ���Ժ�ʹﵽ�����
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
        if isfull == "true" then -- ��������˾Ϳ��Կ�ʼpost��
            if not lualib:HasTimer("0", 90163) then
                lualib:SetDBStrEx("now_dispatcher_data_java", str, 6)
                lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), "", 6)
                lualib:SetDBStrEx("drop_item_table_dispatcher_isfull"..tostring(i), "", 6) -- ֻҪ����true����
                lualib:AddTimerEx("0", 90163, 333, DROP_TABLE_DISJAVA_MAX_NUM, "dispatcher_real_sender_disjava", "")
                index_table[i] = 0
                lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
                break
            else
                -- �������ڷַ��ˣ��Ȳ�������
                break
            end
        elseif i > 1 then
            if lualib:GetDBStr("drop_item_table_dispatcher_isfull" .. tostring(i - 1)) ~= "true" then
                if str ~= "" and str ~= "{}" then
                    local drop_item = json.decode(str)
                    if not lualib:HasTimer("0", 90163) then
                        lualib:SetDBStrEx("now_dispatcher_data_java", str, 6)
                        lualib:SetDBStrEx("drop_item_table_dispatcher_"..tostring(i), "", 6)
                        lualib:SetDBStrEx("drop_item_table_dispatcher_isfull"..tostring(i), "", 6) -- ֻҪ����true����
                        lualib:AddTimerEx("0", 90163, 333, #drop_item, "dispatcher_real_sender_disjava", "")
                        index_table[i] = 0
                        lualib:SetDBStrEx("drop_item_all_index", json.encode(index_table), 6)
                        break
                    else
                        -- �������ڷַ��ˣ��Ȳ�������
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
        local data = "keyname="..tostring(drop_item[1].keyname).."&itemname="..tostring(drop_item[1].itemname).."&count="..tostring(drop_item[1].count).."&zoneid="..tostring(lualib:GetZoneId())
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
--|*   ��Ʒ����ͳ������  created by ken      END              |
--|*-------------------------------------------------------|

--|*-------------------------------------------------------|
--|*   GS BOSS ��ɱͳ�� created by ken       BEGIN          |
--|*-------------------------------------------------------|

function addRecordToServer(monster, killer)
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
--|*   GS BOSS ��ɱͳ��  created by ken      END            |
--|*-------------------------------------------------------|