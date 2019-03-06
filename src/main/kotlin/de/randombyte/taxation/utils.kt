package de.randombyte.taxation

import de.randombyte.kosp.config.serializers.duration.SimpleDurationTypeSerializer
import de.randombyte.kosp.extensions.getServiceOrFail
import de.randombyte.taxation.config.GeneralConfig
import de.randombyte.taxation.config.TextsConfig
import org.slf4j.Logger
import org.spongepowered.api.service.economy.Currency
import org.spongepowered.api.service.economy.EconomyService
import java.time.Duration

val generalConfig: GeneralConfig get() = Taxation.INSTANCE.configAccessor.general.get()
val texts: TextsConfig get() = Taxation.INSTANCE.configAccessor.texts.get()
val logger: Logger get() = Taxation.INSTANCE.logger

fun EconomyService.getCurrencyById(id: String) = currencies.firstOrNull { it.id == id }
val economyService: EconomyService get() = EconomyService::class.getServiceOrFail()
val currency: Currency = generalConfig.currency.let {
    economyService.getCurrencyById(it) ?: throw RuntimeException("Couldn't find currency $it!")
}

val currentMillis: Long get() = System.currentTimeMillis()

fun <K1, K2, V> Map<K1, V>.mapKeysNotNull(mapper: (K1) -> K2?): Map<K2, V> = mapNotNull { (k1, v) ->
    val k2 = mapper(k1) ?: return@mapNotNull null // continue
    return@mapNotNull k2 to v
}.toMap()

fun String.deserializeDuration() = SimpleDurationTypeSerializer.deserialize(this)
fun Duration.serialize(outputMillis: Boolean = true) = SimpleDurationTypeSerializer.serialize(this, outputMillis)