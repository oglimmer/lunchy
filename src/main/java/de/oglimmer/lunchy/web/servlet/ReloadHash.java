package de.oglimmer.lunchy.web.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;

import com.google.common.base.Charsets;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import de.oglimmer.lunchy.services.LunchyVersion;

@Slf4j
@Data
@AllArgsConstructor
class ReloadHash {

	private static final String PARAMETER_NAME = "cacheBuster";

	private static LoadingCache<ReloadHash, String> hashCache = CacheBuilder.newBuilder().build(new CacheLoader<ReloadHash, String>() {
		public String load(ReloadHash key) {
			return calcHash(key);
		}
	});

	private String src;
	private JspContext jspContext;

	public String getReloadHashParameter() {
		return getConcatChar() + PARAMETER_NAME + "=" + getHashValue();
	}

	private String getHashValue() {
		try {
			return LunchyVersion.INSTANCE.isRunsOnDev() ? RandomStringUtils.randomAlphanumeric(8) : hashCache.get(this);
		} catch (ExecutionException e) {
			log.warn("Failed to get cached value for ReloadHash");
			return calcHash(this);
		}
	}

	private char getConcatChar() {
		return src.contains("?") ? '&' : '?';
	}

	@SneakyThrows(value = IOException.class)
	private static String calcHash(ReloadHash data) {
		PageContext pageContext = (PageContext) data.getJspContext();
		String fullQualFileName = pageContext.getRequest().getServletContext().getRealPath(data.getSrc());
		String content = Files.toString(new File(fullQualFileName), Charsets.UTF_8);
		return Hashing.murmur3_32().hashString(content, Charset.defaultCharset()).toString();
	}
}
