/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.api.site;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.utils.ShUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShSiteAPITest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private String sampleSiteId = "c5bdee96-6feb-4894-9daf-3aab6cdd5087";

	private String newSiteId  = "2761f115-198d-4cd8-9566-7d5671764444";

	private Principal mockPrincipal;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		mockPrincipal = Mockito.mock(Principal.class);
		Mockito.when(mockPrincipal.getName()).thenReturn("admin");
	}

	@Test
	public void shSiteList()  throws Exception {	
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/site").principal(mockPrincipal);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	@Test
	public void shSiteStructure()  throws Exception {		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/site/model").principal(mockPrincipal);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
	@Test
	public void shSiteRootFolder() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v2/site/" + sampleSiteId + "/folder").principal(mockPrincipal);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
		
	}
	
	@Test
	public void shSiteEdit() throws Exception {
		mockMvc.perform(get("/api/v2/site/" + sampleSiteId)).andExpect(status().isOk());

	}

	@Test
	public void shSiteExport() throws Exception {
		mockMvc.perform(get("/api/v2/site/" + sampleSiteId + "/export")).andExpect(status().isOk());

	}

	@Test
	public void stage01ShSiteAdd() throws Exception {
		ShSite shSite = new ShSite();
		shSite.setId(newSiteId);
		shSite.setDescription("Test Site");
		shSite.setName("Test");
		shSite.setUrl("http://example.com");

		String requestBody = ShUtils.asJsonString(shSite);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v2/site").principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(requestBody).contentType("application/json")
				.header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}

	@Test
	public void stage02ShSiteUpdate() throws Exception {
		ShSite shSite = new ShSite();
		shSite.setId(newSiteId);
		shSite.setDescription("Test Site2");
		shSite.setName("Test2");
		shSite.setUrl("http://www2.example.com");

		String requestBody = ShUtils.asJsonString(shSite);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v2/site/" + newSiteId).principal(mockPrincipal)
				.accept(MediaType.APPLICATION_JSON).content(requestBody).contentType("application/json")
				.header("X-Requested-With", "XMLHttpRequest");

		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}
	
	@Test
	public void stage03ShSiteDelete() throws Exception {
		mockMvc.perform(delete("/api/v2/site/" + newSiteId)).andExpect(status().isOk());

	}

}