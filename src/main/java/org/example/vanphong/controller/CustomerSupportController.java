package org.example.vanphong.controller;

import org.example.vanphong.model.Conversation;
import org.example.vanphong.model.Customer;
import org.example.vanphong.model.Message;
import org.example.vanphong.model.User;
import org.example.vanphong.repository.ConversationRepository;
import org.example.vanphong.repository.CustomerRepository;
import org.example.vanphong.repository.MessageRepository;
import org.example.vanphong.service.UserService;
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
@RequestMapping("/support")
public class CustomerSupportController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public String supportHome(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
        if (customer == null) {
            
            return "redirect:/profile";
        }

        List<Conversation> conversations = conversationRepository.findByCustomer(customer);
        model.addAttribute("conversations", conversations);

        return "support/list";
    }

    @GetMapping("/new")
    public String newRequestForm() {
        return "support/form";
    }

    @PostMapping("/new")
    public String createRequest(Authentication authentication, @RequestParam String title, @RequestParam String content, RedirectAttributes redirectAttributes) {
        try {
            if (authentication == null) return "redirect:/login";

            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            
            if (user != null) {
                Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
                
                if (customer != null) {
                    Conversation conv = new Conversation();
                    conv.setCustomer(customer);
                    conv.setTitle(title);
                    conv.setStatus("Open");
                    conv.setCreatedDate(new Date());
                    conversationRepository.save(conv);

                    Message msg = new Message();
                    msg.setConversation(conv);
                    msg.setSender(user);
                    msg.setContent(content);
                    msg.setTimestamp(new Date());
                    messageRepository.save(msg);
                    
                    redirectAttributes.addFlashAttribute("message", "Yêu cầu của bạn đã được gửi thành công! Chúng tôi sẽ phản hồi sớm nhất có thể.");
                    return "redirect:/support";
                } else {
                     redirectAttributes.addFlashAttribute("error", "Vui lòng cập nhật đầy đủ thông tin hồ sơ (Tên, SĐT, Địa chỉ) để chúng tôi có thể hỗ trợ bạn tốt nhất.");
                     return "redirect:/profile";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi gửi yêu cầu. Vui lòng thử lại sau hặc liên hệ qua hotline.");
        }
        return "redirect:/support";
    }
    
    
    @GetMapping("/view")
    public String viewConversation(Authentication authentication, @RequestParam Integer id, Model model) {
        if (authentication == null) return "redirect:/login";
        
        Conversation conversation = conversationRepository.findById(id).orElse(null);
        if (conversation == null) return "redirect:/support";
        
        
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
        
        if (customer != null && !conversation.getCustomer().getUserId().equals(customer.getUserId())) {
             return "redirect:/support";
        }
        
        
        List<Message> messages = messageRepository.findByConversation(conversation);
        
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        
        return "support/detail";
    }
    
    @PostMapping("/reply")
    public String reply(Authentication authentication, @RequestParam Integer conversationId, @RequestParam String content) {
        if (authentication == null) return "redirect:/login";
        
        User user = userService.findByEmail(authentication.getName()).orElse(null);
        Conversation conv = conversationRepository.findById(conversationId).orElse(null);
        
        if (user != null && conv != null) {
            Message msg = new Message();
            msg.setConversation(conv);
            msg.setContent(content);
            msg.setSender(user);
            msg.setTimestamp(new Date());
            messageRepository.save(msg);
        }
        return "redirect:/support/view?id=" + conversationId;
    }
}

