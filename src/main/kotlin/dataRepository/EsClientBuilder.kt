package dataRepository

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.create

object EsClientBuilder {

    val restHighLevelClient = create()

}
