package dataRepository

import org.elasticsearch.client.create

object EsClientBuilder {


    val restHighLevelClient = create(https= true,port=443, host = "search-luxuryshop-avxd6cxwsuggilyn3qy4hzaxca.us-west-2.es.amazonaws.com", user = "devops01", password = "94esTh2p9y6gXE4!")
}
