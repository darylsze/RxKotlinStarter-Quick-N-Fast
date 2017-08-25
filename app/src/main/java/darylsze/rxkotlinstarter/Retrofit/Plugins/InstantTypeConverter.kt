package darylsze.rxkotlinstarter.Retrofit.Plugins

import com.google.gson.*
import org.joda.time.Instant
import java.lang.reflect.Type

class InstantTypeConverter : JsonSerializer<Instant>, JsonDeserializer<Instant> {
    override fun serialize(src: Instant, srcType: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.millis)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): Instant {
        return Instant.parse(json.asString)
    }
}