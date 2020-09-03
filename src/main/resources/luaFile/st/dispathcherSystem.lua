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