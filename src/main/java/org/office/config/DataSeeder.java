package org.office.config;

import org.office.model.*;
import org.office.model.ProductType;
import org.office.repository.CategoryRepository;
import org.office.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        seedStaff();
        seedAdmin();
        seedData();
    }

    private void seedAdmin() {
        
        if (userRepository.findByEmail("admin@ergoffice.com").isEmpty()) {
            Role adminRole = roleRepository.findByRoleName("ADMINISTRATOR")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleId(1);
                    r.setRoleName("ADMINISTRATOR");
                    return roleRepository.save(r);
                });

            Staff admin = new Staff();
            admin.setName("Administrator");
            admin.setEmail("admin@ergoffice.com");
            admin.setPassword("admin@ergoffice.com");
            admin.setPhone("0909090909");
            admin.setAddress("Head Office");
            admin.setGender("Male");
            admin.setStatus("Active");
            admin.setIsActive(1);
            admin.setIsDelete(0);
            admin.setRole(adminRole);
            admin.setPosition("Administrator");
            admin.setDepartment("Management");
            
            staffRepository.save(admin);
            System.out.println("✅ Created Admin user: admin@ergoffice.com / 123456");
        }
    }

    private void seedStaff() {
        
        roleRepository.findById(3).ifPresent(role -> {
            if ("STAFF".equalsIgnoreCase(role.getRoleName())) {
                role.setRoleName("CUSTOMER");
                roleRepository.save(role);
                System.out.println("✅ REPAIRED: Renamed Role 3 back to CUSTOMER from STAFF");
            }
        });

        
        Role staffRole = roleRepository.findByRoleName("STAFF").orElse(null);
        if (staffRole == null) {
            staffRole = new Role();
            staffRole.setRoleId(4); 
            staffRole.setRoleName("STAFF");
            roleRepository.save(staffRole);
            System.out.println("✅ Created STAFF role with ID 4");
        }

        
        if (userRepository.findByEmail("staff@ergoffice.com").orElse(null) == null) {
             
             
             
             
             Staff staff = new Staff();
             staff.setName("staff");
             staff.setEmail("staff@ergoffice.com");
             staff.setPassword("{noop}123456"); 
             staff.setPhone("0123456789");
             staff.setAddress("Office");
             staff.setGender("Other");
             staff.setStatus("Active");
             staff.setIsActive(1);
             staff.setIsDelete(0);
             staff.setRole(staffRole);
             staff.setPosition("Manager");
             staff.setDepartment("Sales");
             
             staffRepository.save(staff);
             System.out.println("✅ Created staff user: staff / 123456");
        }

        
        if (userRepository.findByEmail("nhanvien@ergoffice.com").orElse(null) == null) {
             Staff staff2 = new Staff();
             staff2.setName("Nhan Vien");
             staff2.setEmail("nhanvien@ergoffice.com");
             staff2.setPassword("{noop}123123"); 
             staff2.setPhone("0987654321");
             staff2.setAddress("Office 2");
             staff2.setGender("Male");
             staff2.setStatus("Active");
             staff2.setIsActive(1);
             staff2.setIsDelete(0);
             staff2.setRole(staffRole);
             staff2.setPosition("Staff");
             staff2.setDepartment("Support");
             
             staffRepository.save(staff2);
             System.out.println("✅ Created staff user: nhanvien / 123123");
        }
    }

    private void seedData() {
        
        String projectDir = System.getProperty("user.dir");
        String uploadPath = projectDir + File.separator + "uploads" + File.separator + "products";
        File folder = new File(uploadPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("❌ Upload folder not found: " + folder.getAbsolutePath());
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("❌ No files found in uploads/products");
            return;
        }

        System.out.println("🚀 Found " + files.length + " images. Starting seeding...");
        
        
        List<File> fileList = Arrays.asList(files);
        Collections.shuffle(fileList);
        
        
        Map<String, String> prefixCategoryMap = new HashMap<>();
        prefixCategoryMap.put("ban-giam-doc", "Bàn Giám Đốc");
        prefixCategoryMap.put("ban-hop", "Bàn Họp");
        prefixCategoryMap.put("ban-lam-viec", "Bàn Làm Việc");
        prefixCategoryMap.put("ban-le-tan", "Quầy Lễ Tân");
        prefixCategoryMap.put("ban-thu-ngan", "Quầy Thu Ngân");
        prefixCategoryMap.put("ban-tra", "Bàn Trà");
        prefixCategoryMap.put("ban-trang-diem", "Bàn Trang Điểm");
        prefixCategoryMap.put("ghe", "Ghế Văn Phòng");
        prefixCategoryMap.put("giuong", "Giường Ngủ");
        prefixCategoryMap.put("ke", "Kệ Trang Trí");
        prefixCategoryMap.put("quay", "Quầy Lễ Tân");
        prefixCategoryMap.put("tu", "Tủ Tài Liệu");
        prefixCategoryMap.put("combo", "Combo Nội Thất");

        int count = 0;
        for (File file : fileList) {
            if (!file.isFile()) continue;
            
            String fileName = file.getName();
            String lowerName = fileName.toLowerCase();
            
            
            if (!lowerName.endsWith(".jpg") && !lowerName.endsWith(".png") && !lowerName.endsWith(".jpeg")) {
                continue;
            }

            
            String categoryName = "Sản Phẩm Khác";
            for (Map.Entry<String, String> entry : prefixCategoryMap.entrySet()) {
                if (lowerName.startsWith(entry.getKey())) {
                    categoryName = entry.getValue();
                    break;
                }
            }

            Category category = createCategory(categoryName, "Danh mục " + categoryName);
            
            
            
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
            
            String productName = formatName(nameWithoutExt);

            
            List<Product> existingProducts = productRepository.findByName(productName);
            if (!existingProducts.isEmpty()) {
                continue;
            }

            
            Product p = new Product();
            p.setName(productName); 
            p.setDescript("Sản phẩm " + productName + " chất lượng cao, thiết kế hiện đại.");
            p.setCategory(category);
            p = productRepository.save(p);
            
            
            createImage(p, "uploads/products/" + fileName);

            count++;
            
            
            double price = 500000 + (Math.random() * (10000000 - 500000));
            
            price = Math.round(price / 10000) * 10000;

            
            createType(p, "Tiêu Chuẩn", "Standard", price, 50);
            
            if (count % 50 == 0) {
                System.out.println("Processing... " + count + " products created.");
            }
        }
        
        System.out.println("✅ FINISHED! Created " + count + " new products from images.");
    }
    
    private String formatName(String raw) {
        String[] parts = raw.split("-");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1));
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private Category createCategory(String name, String desc) {
        Category existing = categoryRepository.findByCategoryName(name);
        if (existing != null) {
            return existing;
        }
        Category category = new Category();
        category.setCategoryName(name);
        return categoryRepository.save(category);
    }

    private void createImage(Product product, String url) {
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setProductImage(url);
        productImageRepository.save(image);
    }

    private void createType(Product product, String name, String material, Double price, Integer quantity) {
         ProductType type = new ProductType();
         type.setProduct(product);
         type.setColor(name); 
         type.setMaterial(material);
         type.setPrice(price);
         type.setQuantity(quantity);
         productTypeRepository.save(type);
    }
}

