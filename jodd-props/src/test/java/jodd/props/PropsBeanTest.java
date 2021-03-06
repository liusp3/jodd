// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.props;

import jodd.bean.BeanCopy;
import jodd.bean.BeanUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropsBeanTest {

	public static class HttpConfig {
		public int port;
		public String address;
		public int pool;
	}

	@Test
	void testInnerMapToBean() {
		String data = "http.port=10101\n" +
			"http.address=localhost\n" +
			"http.pool=30\n" +
			"foo=bar";

		Props props = new Props();
		props.load(data);

		Map innerMap = props.innerMap("http");
		assertEquals(3, innerMap.size());

		HttpConfig httpConfig = new HttpConfig();

		BeanCopy.fromMap(innerMap).toBean(httpConfig).copy();

		assertEquals(10101, httpConfig.port);
		assertEquals(30, httpConfig.pool);
		assertEquals("localhost", httpConfig.address);

		// back

		props = new Props();
		props.addInnerMap("http", innerMap);

		assertEquals("10101", props.getValue("http.port"));
		assertEquals("30", props.getValue("http.pool"));
		assertEquals("localhost", props.getValue("http.address"));
	}

	@Test
	void testToBean() {
		String data = "port=10101\n" +
			"address=localhost\n" +
			"pool=30\n" +
			"foo=bar";

		final Props props = new Props();
		props.load(data);

		final HttpConfig httpConfig = new HttpConfig();

		props.entries().forEach(pe -> BeanUtil.silent.setProperty(httpConfig, pe.getKey(), pe.getValue()));

		assertEquals(10101, httpConfig.port);
		assertEquals(30, httpConfig.pool);
		assertEquals("localhost", httpConfig.address);
	}
}
