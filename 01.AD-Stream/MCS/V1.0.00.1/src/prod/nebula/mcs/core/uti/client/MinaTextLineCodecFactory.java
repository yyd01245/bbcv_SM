package prod.nebula.mcs.core.uti.client;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

public class MinaTextLineCodecFactory extends TextLineCodecFactory {
	private String encodingDelimiter;
	private String decodingDelimiter;
	private String charset;
	
	public String getEncodingDelimiter() {
		return encodingDelimiter;
	}
	
	public void setEncodingDelimiter(String encodingDelimiter) {
		this.encodingDelimiter = encodingDelimiter;
	}
	
	public String getDecodingDelimiter() {
		return decodingDelimiter;
	}

	public void setDecodingDelimiter(String decodingDelimiter) {
		this.decodingDelimiter = decodingDelimiter;
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public MinaTextLineCodecFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MinaTextLineCodecFactory(String charset,
			LineDelimiter encodingDelimiter, LineDelimiter decodingDelimiter) {
		super(Charset.forName(charset), encodingDelimiter, decodingDelimiter);
		// TODO Auto-generated constructor stub
	}

	public MinaTextLineCodecFactory(String charset, String encodingDelimiter,
			String decodingDelimiter) {		
		super(Charset.forName(charset), encodingDelimiter, decodingDelimiter);
		this.decodingDelimiter = decodingDelimiter;
		this.encodingDelimiter = encodingDelimiter;
		this.charset = charset;
	}

	public MinaTextLineCodecFactory(String charset) {
		super(Charset.forName(charset));
		this.charset = charset;
		// TODO Auto-generated constructor stub
	}
	
}
