package com.gm.botpets.employeeinfo.configuration.swagger;
import static com.google.common.collect.Lists.newArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {
	public  final String MESSAGE_401 = "You are not authorized to view the resource";
	public  final String MESSAGE_403 = "Accessing the resource you were trying to reach is forbidden";
	public  final String MESSAGE_404 = "The resource you were trying to reach is not found";
	public  final String CONTROLLER_PACKAGE = "com.gm.botpets.employeeinfo.controller";
	public  final String APP_TITLE = "Employee Info Service Client";
	public  final String APP_DESCRIPTION = "";
	public  final String APP_VERSION = "";
	public  final String TERMS_OF_SERVICE_URL = "";
	public  final String CONTACT_NAME = "";
	public  final String CONTACT_URL = "";
	public  final String CONTACT_EMAIL = "";
	public  final String APP_LICENSE = "";
	 @Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE)).build().apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, newArrayList(
						new ResponseMessageBuilder().code(401).message(MESSAGE_401).responseModel(new ModelRef("Error"))
								.build(),
						new ResponseMessageBuilder().code(403).message(MESSAGE_403).build(),
						new ResponseMessageBuilder().code(404).message(MESSAGE_404).build()))
				.globalResponseMessage(RequestMethod.POST, newArrayList(
						new ResponseMessageBuilder().code(401).message(MESSAGE_401).responseModel(new ModelRef("Error"))
								.build(),
						new ResponseMessageBuilder().code(403).message(MESSAGE_403).build(),
						new ResponseMessageBuilder().code(404).message(MESSAGE_404).build()))
				.globalResponseMessage(RequestMethod.PUT, newArrayList(
						new ResponseMessageBuilder().code(401).message(MESSAGE_401).responseModel(new ModelRef("Error"))
								.build(),
						new ResponseMessageBuilder().code(403).message(MESSAGE_403).build(),
						new ResponseMessageBuilder().code(404).message(MESSAGE_404).build()))
				.globalResponseMessage(RequestMethod.DELETE, newArrayList(
						new ResponseMessageBuilder().code(401).message(MESSAGE_401).responseModel(new ModelRef("Error"))
								.build(),
						new ResponseMessageBuilder().code(403).message(MESSAGE_403).build(),
						new ResponseMessageBuilder().code(404).message(MESSAGE_404).build()));
	}


	
	 private ApiInfo apiInfo() {
			return new ApiInfoBuilder().title(APP_TITLE)
					.description(APP_DESCRIPTION)
					.termsOfServiceUrl(TERMS_OF_SERVICE_URL)
					 .license(APP_LICENSE)
					.licenseUrl(CONTACT_URL).version(APP_TITLE)
					.build();
		}
}

