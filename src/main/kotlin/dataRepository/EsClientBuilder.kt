package dataRepository

import org.elasticsearch.client.create

object EsClientBuilder {

    val restHighLevelClient = create(host = "es")
}
