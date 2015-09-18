/** 
 * Project: mtp
 * author : PengSong
 * File Created at 2013-10-28 
 *
 * Copyright bbcvision Corporation Limited. 
 * All rights reserved. 
 *
 * This software is the confidential and proprietary information of 
 * bbcvision company. ("Confidential Information"). You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with bbcvision.com. 
 */
package prod.nebula.socket.mina;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

/** 
 * TODO Comment of MinaTextLineCodecFactory 
 * 
 * @author PengSong 
 */
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
		this.setDecoderMaxLineLength(4096);
		this.setEncoderMaxLineLength(4096);	
	}

	public MinaTextLineCodecFactory(String charset,
			LineDelimiter encodingDelimiter, LineDelimiter decodingDelimiter) {
		super(Charset.forName(charset), encodingDelimiter, decodingDelimiter);
		this.setDecoderMaxLineLength(4096);
		this.setEncoderMaxLineLength(4096);
	}
	
	public MinaTextLineCodecFactory(String charset, String encodingDelimiter,
			String decodingDelimiter,int decoderMaxLineLength,int encoderMaxLineLength) {		
		super(Charset.forName(charset), encodingDelimiter, decodingDelimiter);
		this.decodingDelimiter = decodingDelimiter;
		this.encodingDelimiter = encodingDelimiter;
		this.charset = charset;
		this.setDecoderMaxLineLength(decoderMaxLineLength);
		this.setEncoderMaxLineLength(encoderMaxLineLength);
	}
	
	public MinaTextLineCodecFactory(String charset) {
		super(Charset.forName(charset));
		this.setDecoderMaxLineLength(4096);
		this.setEncoderMaxLineLength(4096);
	}
}
