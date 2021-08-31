# UserServiceOpsec

The repo has 2 maven projects
1. OpsecProxyGateway
2. UserService

OpsecProxyGateway - The proxy gateway project, configured to start on port 9000, this will divert call to user service api which runs at port 8080
	-> Instead of Zuul, I have used Spring Cloud Gateway for proxy as per new release in 2020, 
		spring has removed zuul from its spring cloud dependency and recommends using newly created Spring Cloud Gateway.
		https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now#replacements

UserService - This is a multi maven module project. It has 2 modules - UserServiceAPI & UserServiceModel
1. UserServiceAPI - This is the module that hold the auto generated code for the api stub. Uses open api code generator to generate controller stub from open api sec file.
					-> Open api sec file can be found in resources folder with name - api.yaml
					-> Api has 4 endpoints as per the requirement.
					-> Delegate pattern was enabled for code generation of open api to serve as the interface to be extended for implementing business logic
					
		Endpoints:
		1. Create User - creates a new user and returns unique Id for the user
			URL via proxy - http://localhost:9000/user/create
			URL direct - http://localhost:8080/user/create
			Method type - POST
			Request Body Sample - 
				{
					"firstName": "Aayush",
					"lastName": "Rohatgi",
					"title": "Mr",
					"dob": "07-05-1994"
				}
			Response Body Sample - 
				{
					"uniqueId": "22a9f229-0eaa-4f99-848d-f9b852b2d055"
				}
				
		2. Read User - read user details for user with given id
			URL via proxy - http://localhost:8080/user/read/{userId}
			URL direct - https://localhost:8080/user/read/{userId}
			Method type - GET
			Request URL Sample - 
				http://localhost:8080/user/read/22a9f229-0eaa-4f99-848d-f9b852b2d055
			Response Body Sample - 
				{
					"id": "22a9f229-0eaa-4f99-848d-f9b852b2d055",
					"firstName": "Opsec",
					"lastName": "Opsec",
					"dob": "07-05-1990",
					"title": "Mr"
				}
		
		3. Update User - update user details identified by the id
			URL via proxy - http://localhost:8080/user/update/
			URL direct - https://localhost:8080/user/update/
			Method type - POST
			Request URL Sample - 
				{
					"id": "22a9f229-0eaa-4f99-848d-f9b852b2d055",
					"firstName": "Opsec",
					"lastName": "Opsec",
					"title": "Mr",
					"dob": "07-05-1990"
				}
			Response Body Sample - 
				{
					"uniqueId": "a8b20458-5937-417f-b044-d84b647c3958",
					"updated": true
				}
				
		4. Delete User - delete a user for given userId
			URL via proxy - http://localhost:8080/user/delete/{userId}
			URL direct - https://localhost:8080/user/delete/{userId}
			Method type - POST
			Request URL Sample - 
				http://localhost:8080/user/delete/22a9f229-0eaa-4f99-848d-f9b852b2d055
			Response Body Sample - 
					{
						"deleted": true
					}
				
	-> Pass dob in any other format apart from dd-MM-yyyy, you will recieve bad request, for delete user case pass a non existing id and deleted: false will be returned.
	-> For Update user & read user if userId is non existant then bad request will be thrown.
	For more insights into this information please read the api.yaml file

2. UserServiceModel - This module holds all the business logic

-> Mapstruct has also been used in the project for creation of mappers to convert various view model to entity or visa versa
-> UserService class has the major business logic and this class as mentioned earlier extends the auto generated delegate.

Database:
	Spring data Mongo has been used to integrate this application with mongo db
	This application has 1 entity - UserDetailsEntity, which is mapped in mongodb to collection - users
	UserDetailsEntity has following fields:
		String id   // This serves as the id for database as well - For now this is generated randomly through UUID class
		String firstName
		String lastName
		String title
		Date dob

	UserDetailsRepository extends MongoRepository to provide necessary functionality to perform database operations
	In UserDetailsRepository, 3 new methods were added as per necessity: 
		UserDetails deleteById(String id);
		UserDetails findUserById(String id);
		boolean existsById(String integer);
	Apart from these we have used pre provided save method to create and update user entity


JUnits:
For testing JUnit and Mockito has been used. Also we have:
	-> an interface to store contants - Testconstants
	-> a utility class to provide supporting methods - TestUtil
	-> and 2 test classes - UserServiceTest and UserMapperEntityTest containing test methods corresponding to user service and auto generated map struct mapper respectively
	-> test cases for controller has not been added as the controller is an auto generated stub as per api.yaml service contract, 
		all the functionality has been delegated to user service by the controller.


-> Ensure mongodb is running in the background, if it is password protected please add those details to application.properties - currently configured for default mongo configuration
-> perform mvn clean install to build both the project
-> For running user service project please use classpath from its UserServiceModel sub-module.

Thank you