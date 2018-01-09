//package com.playtika.carproxy.web;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.context.WebApplicationContext;
//
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@ComponentScan(value = {
//        "com.playtika.carproxy",
//        "com.playtika.carshop"
//})
//@EnableConfigurationProperties
//@TestPropertySource("/test-application.properties")
//public class CarProxyControllerAPITest {
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//    //private MockMvc mockMvc;
//
//    @Before
//    public void setUp() {
//        //mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }
//
//    @Test
//    public void addedCarDealsAreReturnedToClient() throws Exception {
//
////        // when(service.processCarDealsFile("cardeals.csv")).thenReturn(addedCarDealsId);
////        String ids = mockMvc.perform(post("/cardeals")
////                .param("fileUrl", "cardeals.csv")
////                .contentType("application/json;charset=UTF-8"))
////                .andExpect(status().isOk())
////                .andExpect(content().contentType(("application/json;charset=UTF-8")))
////                .andReturn().getResponse().getContentAsString();
////        //Assert.assertThat(Long.parseLong(ids), is(equalTo(addedCarDealsId)));
////        Assert.assertThat(ids, is(equalTo("[1,2]")));
//
//    }
//    //run second service???
//    //verify call to second service
//}
