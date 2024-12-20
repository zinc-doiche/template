package com.devport.cache.lettuce

import com.devport.config.DataRootInfo
import com.devport.config.asConfigSpec
import com.devport.resources.cache.Config
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.resource.ClientResources
import java.io.File
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

object RedisClientProvider {

    fun get(dataRootInfo: DataRootInfo): RedisClient {
        val configs = Config::class.asConfigSpec(dataRootInfo).load()
            ?: throw IllegalStateException("Cache config를 불러오는 데 실패했어요.")
        val redis = configs.cache.redis

        return RedisClient.create(
            ClientResources.builder()
                .build(),
            RedisURI.Builder.redis(redis.host)
                .withPort(redis.port)
                .withAuthentication(
                    redis.clientName,
                    redis.password as CharSequence
                )
                .withTimeout(configs.cache.timeoutSeconds.seconds.toJavaDuration())
                .build()
        )
    }
}