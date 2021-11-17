package com.example.demo;

import com.example.demo.entity.product.Product;
import com.example.demo.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        productRepository.deleteAll();
    }

    @After
    public void clear() {
        productRepository.deleteAll();
    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    @Test
    public void testCreateProduct() throws Exception{

        JSONObject request = new JSONObject()
                .put("name", "Pie")
                .put("price", 10);

        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.
                        post("/products").
                        headers(httpHeaders).
                        content(request.toString());
        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.get("name")))
                .andExpect(jsonPath("$.price").value(request.get("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void testGetProduct() throws Exception {
        Product requestProduct = createProduct("Apple Pie", 100);
        productRepository.insert(requestProduct);
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + requestProduct.getId()).headers(httpHeaders))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestProduct.getId()))
                .andExpect(jsonPath("$.name").value(requestProduct.getName()))
                .andExpect(jsonPath("$.price").value(requestProduct.getPrice()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    public void testReplaceProduct() throws Exception {
        Product requestProduct = createProduct("Apple Pie", 100);
        productRepository.insert(requestProduct);

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Apple Juice");
        requestBody.put("price", 50);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/products/" + requestProduct.getId())
                .headers(httpHeaders)
                .content(requestBody.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(requestBody.get("name")))
                .andExpect(jsonPath("$.price").value(requestBody.get("price")))
                .andExpect(jsonPath("$.id").value(requestProduct.getId()))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {
        Product product = createProduct("Apple Pie", 100);
        productRepository.insert(product);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/products/" + product.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());
        productRepository.findById(product.getId()).orElseThrow(() -> new RuntimeException(""));

    }

//      重搞一個test, 然後把之前的branch-ProductParameter的bug改掉
    @Test
    public void testSearchProductsSortByPriceAsc() throws Exception {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);

        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/products")
                .headers(httpHeaders)
                .param("keyWord", "Management")
                .param("orderBy", "price")
                .param("sortRule", "asc"))
                .andReturn();
        MockHttpServletResponse httpServletResponse = result.getResponse();
        String httpServletResponseContent = httpServletResponse.getContentAsString();
        JSONArray responseJSONArray = new JSONArray(httpServletResponseContent);

        List<String> ids = new ArrayList<>();

        for (int i = 0; i < responseJSONArray.length(); i++) {
            ids.add(responseJSONArray.getJSONObject(i).getString("id"));
        }

        Assert.assertEquals(ids.size(), 4);
        Assert.assertEquals(ids.get(0), p2.getId());
        Assert.assertEquals(ids.get(1), p1.getId());
        Assert.assertEquals(ids.get(2), p4.getId());
        Assert.assertEquals(ids.get(3), p3.getId());

        Assert.assertEquals(MediaType.APPLICATION_JSON_VALUE, httpServletResponse.getHeader(HttpHeaders.CONTENT_TYPE));
        Assert.assertEquals(HttpStatus.OK.value(), httpServletResponse.getStatus());
    }

    @Test
    public void testSearchProductsSortByNameDesc() throws Exception {
        Product p1 = createProduct("Operation Management", 350);
        Product p2 = createProduct("Marketing Management", 200);
        Product p3 = createProduct("Human Resource Management", 420);
        Product p4 = createProduct("Finance Management", 400);
        Product p5 = createProduct("Enterprise Resource Planning", 440);

        productRepository.insert(Arrays.asList(p1, p2, p3, p4, p5));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/products")
                        .headers(httpHeaders)
                        .param("keyWord", "management")
                        .param("orderBy", "name")
                        .param("sortRule", "desc"))
                        .andReturn();
        MockHttpServletResponse response = result.getResponse();
        String responseContext = response.getContentAsString();
        JSONArray responseArrayJSON = new JSONArray(responseContext);

        List<String> idList = new ArrayList();
        for (int i = 0; i < responseArrayJSON.length(); i++) {
            JSONObject id = responseArrayJSON.getJSONObject(i);
            idList.add(id.getString("id"));
        }

        Assert.assertEquals(p1.getId(), idList.get(0));
        Assert.assertEquals(p2.getId(), idList.get(1));
        Assert.assertEquals(p3.getId(), idList.get(2));
        Assert.assertEquals(p4.getId(), idList.get(3));

        Assert.assertEquals(response.getHeader(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON_VALUE);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }

    @Test
    public void get400WhenCreateProductWithEmptyName() throws Exception {
        JSONObject request = new JSONObject()
                .put("name", "")
                .put("price", 90);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders)
                .content(request.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenReplaceProductWithEmptyPrice() throws Exception {
        Product product = createProduct("Apple pie", 50);
        productRepository.insert(product);

        JSONObject requestBody = new JSONObject()
                .put("name", "Apple Juice")
                .put("price", -1);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/products/" + product.getId())
                .headers(httpHeaders)
                .content(requestBody.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get400WhenCreateProductWithEmptyPrice() throws Exception {
        JSONObject request = new JSONObject()
                .put("name", "apple pie")
                .put("price", null);
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                .headers(httpHeaders)
                .content(request.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
