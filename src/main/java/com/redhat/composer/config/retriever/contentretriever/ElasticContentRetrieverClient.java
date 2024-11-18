package com.redhat.composer.config.retriever.contentretriever;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.redhat.composer.model.request.RetrieverRequest;
import com.redhat.composer.model.request.retriever.ElasticRequest;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.elasticsearch.client.RestClient;

/**
 * Elastic Content Retriever Client.
 */
@Singleton
public class ElasticContentRetrieverClient extends BaseContentRetrieverClient {
  @Inject
  RestClient restClient;

  Logger log = Logger.getLogger(ElasticContentRetrieverClient.class);

  @ConfigProperty(name = "elastic.default.scheme")
  private String elasticScheme;

  @ConfigProperty(name = "elastic.default.host")
  private String elasticHost;

  @ConfigProperty(name = "elastic.default.apiKey")
  private String elasticApiKey;

  @ConfigProperty(name = "elastic.default.index")
  private String elasticIndex;

  @ConfigProperty(name = "elastic.default.textKey")
  private String elasticTextKey;

  /**
   * Get the Content Retriever.
   * 
   * @param request the RetrieverRequest
   * @return the Content Retriever
   */
  public ContentRetriever getContentRetriever(RetrieverRequest request) {
    ElasticRequest elasticRequest = (ElasticRequest) request.getBaseRetrieverRequest();
    if (elasticRequest == null) {
      elasticRequest = new ElasticRequest();
    }
    // String scheme = elasticRequest.getScheme() != null ?
    // elasticRequest.getScheme() : elasticScheme;
    // String host = elasticRequest.getHost() != null ? elasticRequest.getHost() :
    // elasticHost;
    // String apiKey = elasticRequest.getApiKey() != null ?
    // elasticRequest.getApiKey() : elasticApiKey;
    String index = elasticRequest.getIndex() != null ? elasticRequest.getIndex() : elasticIndex;
    // String textKey = elasticRequest.getTextKey() != null ?
    // elasticRequest.getTextKey() : elasticTextKey;

    log.info("Attempting to connect to Elastic at " + restClient.toString() + " with index " + index);

    EmbeddingStore<TextSegment> store = ElasticsearchEmbeddingStore.builder()
        .indexName(index)
        .restClient(restClient)
        .build();

    // Retrieve the embedding model
    EmbeddingModel embeddingModel = getEmbeddingModel(request.getEmbeddingType());

    // Create the content retriever
    ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(store)
        .embeddingModel(embeddingModel)
        .build();

    return contentRetriever;
  }

}
