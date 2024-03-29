/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ae.apps.pnrstatus.vo

import java.io.Serializable

class MessageVo : Serializable {
    @JvmField
	var address: String? = null
    @JvmField
	var name: String? = null
    @JvmField
	var date: String? = null
    @JvmField
	var message: String? = null
    @JvmField
	var type: String? = null

    /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (address == null) 0 else address.hashCode()
        result = prime * result + if (date == null) 0 else date.hashCode()
        result = prime * result + if (message == null) 0 else message.hashCode()
        result = prime * result + if (name == null) 0 else name.hashCode()
        result = prime * result + if (type == null) 0 else type.hashCode()
        return result
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val another = other as MessageVo
        if (address == null) {
            if (another.address != null) return false
        } else if (address != another.address) return false
        if (date == null) {
            if (another.date != null) return false
        } else if (date != another.date) return false
        if (message == null) {
            if (another.message != null) return false
        } else if (message != another.message) return false
        if (name == null) {
            if (another.name != null) return false
        } else if (name != another.name) return false
        if (type == null) {
            if (another.type != null) return false
        } else if (type != another.type) return false
        return true
    }

    companion object {
        private const val serialVersionUID = -8006234704930944248L
    }
}