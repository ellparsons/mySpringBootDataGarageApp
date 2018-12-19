package integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner; 
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.parsons.elliott.springboot.garage.mySpringBootGarageApp.MySpringBootGarageAppApplication;
import com.parsons.elliott.springboot.garage.mySpringBootGarageApp.model.MySpringBootDataModel;
import com.parsons.elliott.springboot.garage.mySpringBootGarageApp.repository.MySpringBootRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MySpringBootGarageAppApplication.class})
@ContextConfiguration(classes = {MySpringBootGarageAppApplication.class})
@AutoConfigureMockMvc
public class IntegrationTest {
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private MySpringBootRepository repository;
	
	@Before
	public void clearDB() 
	{
		repository.deleteAll();  
	}  
	  
	//Test to create a vehicle// 
	@Test
	public void addAVehicleToDatbaseTest() 
		throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/create/vehicle")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"type\" : \"Motorcycle\",\"make\" : \"Kawasaki\", \"model\" : \"600R\"}"))
			.andExpect(status()
			.isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.model", is("600R"))); 
		}  
	
	//Test to get all vehicles//
	@Test
	public void findingAllVehicleFromDatabase()
		throws Exception {
		
		MySpringBootDataModel carVehicle = new MySpringBootDataModel("Car","BMW","M5");
		MySpringBootDataModel motorcycleVehicle = new MySpringBootDataModel("Motorcycle","Yamaha","R6");
		
		repository.save(carVehicle);
		repository.save(motorcycleVehicle);		
		mvc.perform(get("/api/vehicle")
    		.contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status() 
    		.isOk())
			.andExpect(content()  
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].type",is("Car")))
			.andExpect(jsonPath("$[1].type", is("Motorcycle")));
    }  
	  
	//Test to get a vehicle by ID//
    @Test
    public void findingAVehicleFromDatabaseById()
    	throws Exception {
    	
    	MySpringBootDataModel vehicle = new MySpringBootDataModel("Car","BMW","M6");
    	
    	repository.save(vehicle);
    	mvc.perform(get("/api/vehicle/id/" + vehicle.getId())
    		.contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status()
    		.isOk())
    		.andExpect(content()
    		.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) 
    		.andExpect(jsonPath("$.id",is(10))); 
    } 
	
	//Test to get a vehicle by type//   
	@Test 
	public void findingAVehicleFromDatabaseByType()    
		throws Exception { 
		repository.save(new MySpringBootDataModel("Car","BMW","M5"));  
		mvc.perform(get("/api/vehicle/type/Car") 
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status() 
			.isOk())  
			.andExpect(content()  
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].type",is("Car")));		   
	      }   	 
  	
	//Test to get a vehicle by make//
	@Test  
	public void findingAVehicleFromDatabaseByMake()    
		throws Exception { 
		repository.save(new MySpringBootDataModel("Car","BMW","M4"));  
		mvc.perform(get("/api/vehicle/make/BMW") 
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status()  
			.isOk()) 
			.andExpect(content() 
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].make",is("BMW")));		   
	      } 
	       
	//Test to get a vehicle by model//
	@Test 
	public void findingAVehicleFromDatabaseByModel()     
		throws Exception { 
		repository.save(new MySpringBootDataModel("Car","BMW","M3"));   
		mvc.perform(get("/api/vehicle/model/M3") 
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status() 
			.isOk()) 
			.andExpect(content() 
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].model",is("M3")));		  
	      } 
	 
	//Test to update a vehicle// 
	@Test
	public void updatingAVehicleFromDatabase()
		throws Exception {
			
		MySpringBootDataModel vehicle = new MySpringBootDataModel("Car","BMW","M2");
			
		repository.save(vehicle);		
		mvc.perform(put("/api/vehicle/" + vehicle.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"type\" : \"Motorcycle\",\"make\" : \"Kawasaki\", \"model\" : \"H2\"}"))
			.andExpect(status().isOk()); 
 		
		mvc.perform(get("/api/vehicle/id/" + vehicle.getId())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status()  
			.isOk())
			.andExpect(content()
			.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) 
			.andExpect(jsonPath("$.type",is("Motorcycle")));
	}
	 
	//Test to delete a vehicle//
	@Test
	public void deletingAVehicleFromDatabase() 
		throws Exception {
		
		MySpringBootDataModel vehicle = new MySpringBootDataModel("Car","BMW","M1");
		
		repository.save(vehicle);
		mvc.perform(MockMvcRequestBuilders
			.delete("/api/vehicle/" + vehicle.getId())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status() 
			.isOk()); 
		}
	 
	//Test to delete a vehicle by type//
	@Test
	public void deletingAVehicleFromDatabaseByType()
		throws Exception {
		
		MySpringBootDataModel vehicle = new MySpringBootDataModel("Car","BMW","M0");
		
		repository.save(vehicle);
		mvc.perform(MockMvcRequestBuilders 
			.delete("/api/vehicle/type/" + vehicle.getType())
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status()
			.isOk());
	} 

	//Test for ResourceNotFoundException//
	@Test
	public void returningAResourceNotFoundException() 
		throws Exception { 
		
		MySpringBootDataModel vehicle = new MySpringBootDataModel("Car","Honda","Civic");
		
		repository.save(vehicle);
    	mvc.perform(get("/api/vehicle/model/Test")
    		.contentType(MediaType.APPLICATION_JSON))
    		.andExpect(status()
    		.isOk())
    		.andExpect(content()
    		.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)) 
    		.andExpect(jsonPath("$.resourceName",is("200")));  	 
	}
	
}   
