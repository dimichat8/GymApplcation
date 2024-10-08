package com.gym.app.email.EmailService.EmailServiceImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.gym.app.customer.entity.Customer;
import com.gym.app.customer.repository.CustomerRepository;
import com.gym.app.dto.EmailDto;
import com.gym.app.email.EmailService.EmailService;
import jakarta.activation.FileDataSource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username")
    private String fromEmail;

    @Value("${spring.mail.charset}")
    private String charset;

    @Value("${spring.mail.path}")
    private String path;

    @Value("${spring.mail.width}")
    private int width;

    @Value("${spring.mail.height}")
    private int height;

    @Autowired
    private CustomerRepository customerRepository;

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Override
    public ResponseEntity<EmailDto> sendEmail(EmailDto emailDto) {
        try {
            Optional<Customer> customer = customerRepository.findOptionalCustomerByEmail(emailDto.getEmail());
            Customer customerEntity = null;
            String fileName = "";
            if (customer.isPresent()) {
                customerEntity = customer.get();
                boolean customerEnabled = customerEntity.getIsEnabled();
                log.info("Is customer enabled? {}", customerEnabled);
                String data = emailDto.getData();
                if (data == null || data.isEmpty()) {
                    emailDto.setData(customer.get().getFirstname() + "_" + customer.get().getSurname() + " is " + (customerEnabled ? "active" : "inactive"));
                }
                fileName = customer.get().getFirstname() + "_" + customer.get().getSurname() + ".png";
                emailDto.setPath(path + fileName);
            }

            generateQRCode(emailDto);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(emailDto.getEmail());
            mimeMessageHelper.setSubject(emailDto.getSubject() + " " + customerEntity.getFirstname());
            mimeMessageHelper.setText(emailDto.getBody(), true);

            Path filePath = Paths.get(path, fileName);
            mimeMessageHelper.addAttachment(fileName, new FileDataSource(filePath.toFile()));


            String emailContent = emailDto.getBody() + "<br><br>"
                    /*"<img src='cid:qrCodeImage' />"*/;

            mimeMessageHelper.setText(emailContent, true);

            mailSender.send(mimeMessage);
            return ResponseEntity.ok(emailDto);

        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.badRequest().body(emailDto);
        }
    }

    public void generateQRCode(EmailDto emailDto) throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(emailDto.getData().getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToFile(
                matrix,
                emailDto.getPath().substring(emailDto.getPath().lastIndexOf('.') + 1),
                new File(emailDto.getPath()));
        System.out.println("QR Code Generated!!! ");
    }
}