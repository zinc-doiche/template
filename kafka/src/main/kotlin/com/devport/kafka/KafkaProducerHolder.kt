package com.devport.kafka

import com.devport.config.DataRootInfo
import com.devport.config.asConfigSpec
import com.devport.resources.kafka.Config
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

class KafkaProducerHolder {
    lateinit var producer: Producer<String, String>
        private set

    lateinit var config: Config
        private set

    fun initProducer(dataRootInfo: DataRootInfo) {
        val properties = Properties()
        val serializerName = StringSerializer::class.java.name

        config = Config::class.asConfigSpec(dataRootInfo).load()
            ?: throw IllegalStateException("config 로드 실패")

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "${config.host}:${config.port}")
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializerName)
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializerName)

        producer = KafkaProducer<String, String>(properties)
    }
}