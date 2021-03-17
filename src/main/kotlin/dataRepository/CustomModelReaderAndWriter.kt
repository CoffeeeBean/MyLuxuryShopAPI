package com.jillesvangurp.eskotlinwrapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import kotlin.reflect.KClass

/**
 * Simple implementation of [ModelReaderAndWriter] that uses a jackson object mapper with CAMEL CASE.
 */
class CustomModelReaderAndWriter<T : Any>(
    override val clazz: KClass<T>,
    val objectMapper: ObjectMapper
) : ModelReaderAndWriter<T> {

    /**
     * Alternate constructor so we can instantiate from Java as well
     */
    constructor(javaClazz: Class<T>, objectMapper: ObjectMapper) : this(javaClazz.kotlin, objectMapper)

    override fun deserializer(): (ByteArray?) -> T = { bytes ->
        objectMapper.readValue(bytes, clazz.java)!!
    }
    override fun serializer(): (T) -> ByteArray = { value ->
        if (value is String) {
            // assume it is already json
            value.toByteArray()
        } else {
            objectMapper.writeValueAsBytes(value)
        }
    }

    companion object {
        inline fun <reified T : Any> create(objectMapper: ObjectMapper? = null): CustomModelReaderAndWriter<T> {
            return if (objectMapper == null) {
                val om = ObjectMapper().findAndRegisterModules()
                // sane default, if you want camelcase, override the objectmapper
                om.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
                CustomModelReaderAndWriter<T>(T::class, om)
            } else {
                CustomModelReaderAndWriter<T>(T::class, objectMapper)
            }
        }
    }
}
