--|*-------------------------------------------------------|
--|*   聊天记录必要内容  created by ken         END           |
--|*-------------------------------------------------------|
-- 文件名 ChatWindowEX.lua
if _SendMsg ~= nil and _SendMsg ~= "" then
    UI:Lua_SubmitForm("Common", "receiveChatRecord", _SendMsg.."#"..tostring(ChatWindowEX.ChatChannel))
end

function ChatWindowEX.OnSendMsgPre(x,y,z)
    if y == 13 then
        ChatWindowEX.OnSendMsg()
    end
end

function ChatWindowEX.OnSendMsg(this)
    local _WndEditHandle = GUI:WndFindChildM(ChatWindowEX.WndHandle, "ChatInputEdit")
    if _WndEditHandle ~= 0 then
        local _SendMsg = GUI:EditGetTextM(_WndEditHandle)
        CL:ChatWindowSendMessage(_SendMsg, ChatWindowEX.ChatChannel)
        if _SendMsg ~= nil and _SendMsg ~= "" then
            UI:Lua_SubmitForm("Common", "receiveChatRecord", _SendMsg.."#"..tostring(ChatWindowEX.ChatChannel))
        end
        GUI:EditSetTextM(_WndEditHandle, "")
    end
end

GUI:WndRegistScript(_GUIHandle,RDWndBaseCL_mouse_lbClick,"ChatWindowEX.OnSendMsg")
--|*-------------------------------------------------------|
--|*   聊天记录必要内容  created by ken         END           |
--|*-------------------------------------------------------|

function receiveChatRecord(player, content, channel)
    lualib:DelayCall(player, 1, "dispatcherSystem:insertChatRecordData", content.."#"..channel)
end