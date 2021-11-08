package com.example.demo;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ObjectMapperTest {
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class Book {
        private String id;
        private String name;
        private int price;
        private String isbn;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;
        @JsonUnwrapped
        private Publisher publisher;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public Publisher getPublisher() {
            return publisher;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
        }
    }

    private static class Publisher {
        private String companyName;
//        @JsonIgnore
        private String address;
        @JsonProperty("telephone")
        private String tel;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

    @Test
    public void testSerializeBookToJSON() throws Exception {
        Book book = new Book();

        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        String bookStr = objectMapper.writeValueAsString(book);
        JSONObject bookJSON = new JSONObject(bookStr);

        Assert.assertEquals(book.getId(), bookJSON.getString("id"));
        Assert.assertEquals(book.getName(), bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(), bookJSON.getInt("price"));
        Assert.assertEquals(book.getIsbn(), bookJSON.getString("isbn"));
        Assert.assertEquals(book.getCreatedTime().getTime(), bookJSON.getLong("createdTime"));
    }

    @Test
    public void testDeserializeJSONToPublisher() throws Exception {
        JSONObject publisherJSON = new JSONObject()
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("tel", "02-1234-5678");
        String publisherStr = publisherJSON.toString();
        Publisher publisher = objectMapper.readValue(publisherStr, Publisher.class);

        System.out.println("publisherStr = " + publisherStr);

        Assert.assertEquals(publisher.getTel(), publisherJSON.getString("tel"));
        Assert.assertEquals(publisher.getAddress(), publisherJSON.getString("address"));
        Assert.assertEquals(publisher.getCompanyName(), publisherJSON.getString("companyName"));
    }

    @Test
    public void testSerializePublisherToJSONWithJSONProperty() throws Exception {
        Publisher publisher = new Publisher();
        publisher.setTel("02-1234-5678");
        publisher.setAddress("Taipei");
        publisher.setCompanyName("Taipei Company");

        String publisherStr = objectMapper.writeValueAsString(publisher);

        JSONObject publisherJSON = new JSONObject(publisherStr);

        Assert.assertEquals(publisherJSON.getString("telephone"), publisher.getTel());
        System.out.println("publisherStr = " + publisherStr);
    }

    @Test
    public void testSerializeWithWrapped() throws Exception {
        Book book = new Book();

        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
//        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        Publisher publisher = new Publisher();
        publisher.setTel("02-1234-5678");
        publisher.setAddress("Taipei");
        publisher.setCompanyName("Taipei Company");

        book.setPublisher(publisher);

        String bookStr = objectMapper.writeValueAsString(book);
        JSONObject bookJson = new JSONObject(bookStr);

        System.out.println("bookJson = " + bookJson);

    }
}
