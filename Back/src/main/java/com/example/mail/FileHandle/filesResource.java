package com.example.mail.FileHandle;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_DISPOSITION;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/file")
public class filesResource {
//Define location
    //id
    Gson gson = new Gson();

    public filesResource() throws IOException {
        System.out.println("Hi");
        (new File(System.getProperty("user.home")+"/Downloads/System")).mkdir(); // create system folder if it is not existed.
        (new File(System.getProperty("user.home")+"/Downloads/uploads/")).mkdir(); // create uploads folder if it is not existed.
        (new File(System.getProperty("user.home")+"/Downloads/uploads/firstUser")).mkdir(); // create uploads folder if it is not existed.
        System.out.println(new File(System.getProperty("user.home")+"/Downloads/uploads/firstUser").exists());


        // ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.writeValue(new File(System.getProperty("user.home")+"//Downloads//tr.json"), id.getIds());

        // id.setIds(Files.readString(Paths.get(System.getProperty("user.home")+"//Downloads//tr.json")));


    }
    public static final String Directory=System.getProperty("user.home")+"/Downloads/uploads/";
// Define method to upload
    @PostMapping("/upload")
    public ResponseEntity<List<String>> upload(@RequestParam("files")List<MultipartFile> MultipartFiles) throws IOException {
        List<String> filenames=new ArrayList<>();
        for(MultipartFile file:MultipartFiles){
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage =get(Directory,filename).toAbsolutePath().normalize();
            copy(file.getInputStream(),fileStorage,REPLACE_EXISTING);
            filenames.add(filename);
        }
        return ResponseEntity.ok().body(filenames);
    }
// Define method to download
    @GetMapping("/download/{filename}")
    public ResponseEntity<UrlResource> downloadfilename(@PathVariable("filename") String filename) throws IOException {
        Path filePath =get(Directory).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)){
            throw new FileNotFoundException(filename+" was not found on the server");
        }
        UrlResource resource=new UrlResource(filePath.toUri());
      //  Resource resource = (Resource) res;
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("File-Name",filename);
        httpHeaders.add(CONTENT_DISPOSITION,"attachment;File-Name="+filename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
}
