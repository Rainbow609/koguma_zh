/*
 * Copyright (C) 2022 ghostbear
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package me.ghostbear.koguma.domain.model

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
