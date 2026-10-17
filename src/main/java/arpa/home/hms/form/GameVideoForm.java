package arpa.home.hms.form;

import java.time.LocalDateTime;

public record GameVideoForm(Long id,String title,String name,String fileName,LocalDateTime createdAt,LocalDateTime updatedAt,String videoUrl){}
