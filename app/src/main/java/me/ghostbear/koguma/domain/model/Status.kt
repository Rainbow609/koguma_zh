/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = StatusAsStringSerializer::class)
enum class Status {
    Unknown,
    Ongoing,
    Completed,
    Licensed,
    PublishingFinished,
    Cancelled,
    OnHaitus;

    companion object {

        val values = values()

        fun valueOf(ordinal: Int): Status {
            return values().find { it.ordinal == ordinal } ?: Unknown
        }
    }
}

object StatusAsStringSerializer : KSerializer<Status> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("status", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Status) {
        val string = "${value.ordinal}"
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): Status {
        val string = decoder.decodeString()
        return Status.valueOf(string.toIntOrNull() ?: 0)
    }
}