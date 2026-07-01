package com.Comp_Emp_Manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiService {

	@Value("${nvidia.api.key}")
	private String nvidiaApiKey;

	public String predictAttrition(Long employeeId) {
		// Integration logic with NVIDIA NIM or OpenAI
		// Mocking response for NexusHR prototype
		double risk = Math.random() * 100;
		return "Attrition Risk for ID " + employeeId + " is " + String.format("%.2f", risk) + "%";
	}

	public String chatWithAi(String userMessage, long totalEmployees, long totalLeaves, long pendingLeaves) {
		String systemPrompt = "You are NexusHR AI, an intelligent Human Resources assistant. "
				+ "You have access to the following real-time company statistics: " + "Total Employees: "
				+ totalEmployees + ", " + "Total Leave Requests: " + totalLeaves + " (" + pendingLeaves + " pending). "
				+ "Answer the user's questions about HR, company policies, or the provided statistics concisely and professionally. "
				+ "If they ask something completely unrelated to HR or the company, politely steer the conversation back to HR topics.";

		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(nvidiaApiKey);

			String requestBody = "{\n" + "  \"model\": \"meta/llama-3.1-70b-instruct\",\n" + "  \"messages\": [\n"
					+ "    {\"role\": \"system\", \"content\": \"" + systemPrompt.replace("\"", "\\\"") + "\"},\n"
					+ "    {\"role\": \"user\", \"content\": \"" + userMessage.replace("\"", "\\\"").replace("\n", " ")
					+ "\"}\n" + "  ],\n" + "  \"max_tokens\": 512,\n" + "  \"temperature\": 0.5\n" + "}";

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<Map> response = restTemplate
					.postForEntity("https://integrate.api.nvidia.com/v1/chat/completions", entity, Map.class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				// Safely suppressing the unchecked warning because we know the structure of the
				// NVIDIA API response
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
				if (choices != null && !choices.isEmpty()) {
					@SuppressWarnings("unchecked")
					Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
					return (String) messageObj.get("content");
				}
			}
			return "I'm sorry, I couldn't generate a response at the moment.";
		} catch (Exception e) {
			log.error("Error communicating with the AI service: {}", e.getMessage(), e);
			return "Error communicating with the AI service: " + e.getMessage();
		}
	}
}