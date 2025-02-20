package com.redhat.composer.config.llm.aiservices;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Factory for AI Services.
 */
@ApplicationScoped
public class AiServicesFactory {

  public static final String MISTRAL7B_AI_SERVICE = "mistral7b";

  public static final String MISTRAL7B_QUARKUS_AI_SERVICE = "mistral7b_quarkus";

  public static final String HEALTHCARE_SERVICE = "healthcare";

  /**
   * Get the AI service class.
   * @param aiServiceType the AI service type
   * @return the AI service class
   */
  public Class<? extends BaseAiService> getAiService(String aiServiceType) {
    switch (aiServiceType) {
      case MISTRAL7B_AI_SERVICE:
        return Mistral7bAiService.class;
      case HEALTHCARE_SERVICE:
        return HealthCareService.class;
      default:
        throw new RuntimeException("AI service type not found: " + aiServiceType);
    }
  }
  
}
