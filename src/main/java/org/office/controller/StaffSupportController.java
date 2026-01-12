package org.office.controller;

import org.office.model.Conversation;
import org.office.model.Message;
import org.office.model.User;
import org.office.repository.ConversationRepository;
import org.office.repository.MessageRepository;
import org.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/staff/support")
public class StaffSupportController {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String listRequests(Model model) {
        
        List<Conversation> conversations = conversationRepository.findAll();
        
        
        model.addAttribute("conversations", conversations);
        return "staff/staff-support-list";
    }
    
    @GetMapping("/view")
    public String viewRequest(@RequestParam Integer id, Model model) {
        Conversation conversation = conversationRepository.findById(id).orElse(null);
        if (conversation == null) {
            return "redirect:/staff/support";
        }
        
        List<Message> messages = messageRepository.findByConversation(conversation);
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        
        return "staff/support-detail";
    }

    @PostMapping("/reply")
    public String replyRequest(Authentication authentication, @RequestParam Integer conversationId, @RequestParam String content, RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";

        User staff = userService.findByEmail(authentication.getName()).orElse(null);
        Conversation conv = conversationRepository.findById(conversationId).orElse(null);

        if (staff != null && conv != null) {
            Message msg = new Message();
            msg.setConversation(conv);
            msg.setSender(staff);
            msg.setContent(content);
            msg.setTimestamp(new Date());
            messageRepository.save(msg);
            
            
            if ("Closed".equals(conv.getStatus())) {
                 conv.setStatus("Open");
                 conversationRepository.save(conv);
            }
            
            redirectAttributes.addFlashAttribute("message", "Đã gửi phản hồi thành công.");
        } else {
             redirectAttributes.addFlashAttribute("error", "Lỗi gửi phản hồi.");
        }
        return "redirect:/staff/support/view?id=" + conversationId;
    }
    
    @PostMapping("/close")
    public String closeRequest(@RequestParam Integer conversationId, RedirectAttributes redirectAttributes) {
        Conversation conv = conversationRepository.findById(conversationId).orElse(null);
        if (conv != null) {
            conv.setStatus("Closed");
            conversationRepository.save(conv);
            redirectAttributes.addFlashAttribute("message", "Đã đóng yêu cầu hỗ trợ.");
        }
        return "redirect:/staff/support/view?id=" + conversationId;
    }
}

