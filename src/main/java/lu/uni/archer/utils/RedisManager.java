package lu.uni.archer.utils;

import redis.clients.jedis.Jedis;

/*-
 * #%L
 * Archer
 *
 * %%
 * Copyright (C) 2022 Jordan Samhi
 * University of Luxembourg - Interdisciplinary Centre for
 * Security Reliability and Trust (SnT) - TruX - All rights reserved
 *
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

public class RedisManager {

    private final Jedis jedis;

    public RedisManager(String server, String port, String auth) {
        this.jedis = new Jedis(String.format("redis://%s:%s", server, port));
        this.jedis.auth(auth);
        this.jedis.connect();
    }

    public void send(String list, String val) {
        this.jedis.select(0);
        this.jedis.lpush(list, val);
    }
}
