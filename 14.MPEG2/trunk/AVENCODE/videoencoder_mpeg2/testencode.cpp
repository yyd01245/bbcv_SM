#include <stdio.h>
#include <stdlib.h>

#ifndef INT64_C
#define INT64_C(c) c##LL
#endif
#ifndef UINT64_C
#define UINT64_C(c) c##LL
#endif

#include <stdint.h>
extern "C" {
	#include "libavutil/imgutils.h"
	#include "libswscale/swscale.h"
	#include "libavcodec/avcodec.h"
}
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef void video_encoder_t;
	int n_video_encode_sample = 0;
	int n_video_source_sample = 0;
	const int n_video_encode_sample_size = 5*1024*1024;
	const int n_video_source_sample_size = 5*1024*1024;
	char *p_video_source_sample = NULL;
	char *p_video_encode_sample = NULL;
		video_encoder_t* p_video_encoder = NULL;

		typedef struct
{
	int width;
	int height;
	int gopsize_min;
	int gopsize_max;
	int bitrate;//bps
	int fps;
}InputParams_videoencoder;
		
typedef struct
{
	int width;
	int height;
	int gopsize_min;
	int gopsize_max;
	int bitrate;//bps
	int fps;

	AVCodec *codec;
	AVCodecContext *codecContext;
	AVFrame *picture;

	long i_pts;
	
	char parameter_preset[32];
	char parameter_tune[32];
	char parameter_profile[32];
	long colorspace;

	unsigned char *yuv_buffer;
	int yuv_buffer_size;
	unsigned char *yuv_part_buffer;
	int yuv_part_buffer_size;
	//for statistics
	unsigned long real_count_fps;
	
}videoencoder_instanse;


static int gobale_FrameCount =0;

static int BGRA_to_YUV420SP(int width,int height,const char *pSrc,int iSrc,char *pDst,int *iDst)
{
/*	if(pSrc == NULL || pDst == NULL || iDst == NULL)
	{        
		fprintf(stderr ,"libaudioencoder: Error paraments..\n");        
		return -1;    
	}
	if(width == 0 || height == 0)
	{
		return 0;
	}
	

	//fill picture
	AVPicture picture_src;
	AVPicture picture_dst;
	int ret1 = avpicture_fill(&picture_src, (uint8_t*)pSrc,PIX_FMT_RGB32,width, height);
	int ret2 = avpicture_fill(&picture_dst, (uint8_t*)pDst,PIX_FMT_NV12,width, height);
	if (ret1 < 0 || ret2 < 0)	 
	{ 
		fprintf(stderr ,"libvideoencoder: ffmpeg avpicture_fill error..\n");
		return -2;	
	}

	SwsContext* m_pSwsContext = sws_getContext(width, height, PIX_FMT_RGB32,width,height, PIX_FMT_NV12,SWS_BICUBIC,NULL, NULL, NULL); 							
	if (NULL == m_pSwsContext)	 
	{ 
		fprintf(stderr ,"libvideoencoder: ffmpeg sws_getContext error..\n");
		return -3;	
	}
	
	sws_scale(m_pSwsContext, picture_src.data,picture_src.linesize, 0,  height,picture_dst.data, picture_dst.linesize);  
            
	sws_freeContext(m_pSwsContext);

*/	
	return 0;
}

video_encoder_t* init_video_encoder(InputParams_videoencoder *pInputParams)
{
	int ret = -1;
	if(pInputParams == NULL)
	{        
		fprintf(stderr ,"libvideoencoder: Error paraments..\n");        
		return NULL;    
	}

	

	videoencoder_instanse *p_instanse = (videoencoder_instanse *)malloc(sizeof(videoencoder_instanse));
	if(p_instanse == NULL)
	{        
		fprintf(stderr ,"libvideoencoder: malloc videoencoder_instanse error..\n");        
		return NULL;    
	}

	memset(p_instanse,0,sizeof(videoencoder_instanse));

	p_instanse->width = pInputParams->width;
	p_instanse->height = pInputParams->height;
	p_instanse->gopsize_min = pInputParams->gopsize_min;
	p_instanse->gopsize_max = pInputParams->gopsize_max;
	p_instanse->bitrate = pInputParams->bitrate;
	p_instanse->fps = pInputParams->fps;
	p_instanse->i_pts = 0;

	p_instanse->yuv_buffer_size = p_instanse->width*p_instanse->height*3/2;
	p_instanse->yuv_part_buffer_size = p_instanse->width*p_instanse->height*3/2;
	p_instanse->yuv_buffer = (unsigned char *)malloc(p_instanse->yuv_buffer_size);
	p_instanse->yuv_part_buffer = (unsigned char *)malloc(p_instanse->yuv_buffer_size);
	if(p_instanse->yuv_buffer == NULL || p_instanse->yuv_part_buffer == NULL)
	{        
		fprintf(stderr ,"libvideoencoder: Error malloc..\n");        
		return NULL;    
	}
	p_instanse->codec = NULL;
	p_instanse->codecContext = NULL;
	avcodec_register_all();

	p_instanse->codec = avcodec_find_encoder(CODEC_ID_MPEG2VIDEO);
	if (!p_instanse->codec) {
        fprintf(stderr, "codec not found\n");
        //exit(1);
        return NULL; 
    }

	p_instanse->codecContext = avcodec_alloc_context3(p_instanse->codec);
	if(p_instanse->codecContext == NULL)
	{
		fprintf(stderr ,"libvideoencoder: Mpeg2_encoder_open error..\n");        
		return NULL; 
	}
	p_instanse->picture= avcodec_alloc_frame();

	    /* put sample parameters */
    p_instanse->codecContext->bit_rate = p_instanse->bitrate;
	p_instanse->codecContext->rc_max_rate = p_instanse->bitrate;
	p_instanse->codecContext->rc_min_rate = p_instanse->bitrate;
	p_instanse->codecContext->bit_rate_tolerance = p_instanse->bitrate;
	

	p_instanse->codecContext->rc_buffer_size = p_instanse->bitrate;
	p_instanse->codecContext->rc_initial_buffer_occupancy  = p_instanse->bitrate*3/4;
	p_instanse->codecContext->rc_buffer_aggressivity = (float)1.0;
	p_instanse->codecContext->rc_initial_cplx= 0.5;


//	p_instanse->codecContext->
    /* resolution must be a multiple of two */
    p_instanse->codecContext->width = p_instanse->width;
    p_instanse->codecContext->height = p_instanse->height;
    /* frames per second */
    p_instanse->codecContext->time_base= (AVRational){1,25};
    p_instanse->codecContext->gop_size = 15;	//pInputParams->gopsize_max; /* emit one intra frame every ten frames */
    p_instanse->codecContext->max_b_frames=0;
    p_instanse->codecContext->pix_fmt = PIX_FMT_YUV420P;  //PIX_FMT_RGB24


	printf("------Use mpeg2 soft ,code bitrate=%d \n", p_instanse->codecContext->bit_rate);
	/* open it */
    if (avcodec_open2(p_instanse->codecContext, p_instanse->codec, NULL) < 0) {
        fprintf(stderr, "could not open codec\n");
        //exit(1);
        return NULL;
    }

	 /* the image can be allocated by any means and av_image_alloc() is
     * just the most convenient way if av_malloc() is to be used */
    av_image_alloc(p_instanse->picture->data, p_instanse->picture->linesize,p_instanse->codecContext->width,
    			p_instanse->codecContext->height, p_instanse->codecContext->pix_fmt, 1);
	

	return p_instanse;
}

int encode_video_sample(video_encoder_t *videoencoder,unsigned char *input_video_sample,int input_video_sample_size,
	char *output_video_sample,int *output_video_sample_size)
{
	if(videoencoder == NULL || input_video_sample == NULL || output_video_sample == NULL || output_video_sample_size == NULL)
	{        
		fprintf(stderr ,"libvideoencoder: Error paraments..\n");        
		return -1;    
	}
	videoencoder_instanse *p_instanse = (videoencoder_instanse *)videoencoder;

	if(input_video_sample_size != p_instanse->width*p_instanse->height*3/2)
	{		 
		fprintf(stderr ,"libvideoencoder: input_video_sample_size=%d..but bgra size is width*height*4=%d \n",input_video_sample_size,p_instanse->width*p_instanse->height*4);		
		return -2;	  
	}

	int ret = -1;
	int all_size = 0;
	int nnal = 0;

	int yuv_buffer_size = p_instanse->yuv_buffer_size;
	char *outbf = (char *)p_instanse->yuv_buffer;

	try
	{

		int width = p_instanse->codecContext->width;
		int height = p_instanse->codecContext->height;
		
		p_instanse->picture->data[0] =input_video_sample;
		p_instanse->picture->data[1] =input_video_sample+width*height;
		p_instanse->picture->data[2] =input_video_sample+width*height*5/4;
		//fill buff

		int outbuf_size = 0;
		int out_size = 0;
		gobale_FrameCount++;
		out_size = avcodec_encode_video(p_instanse->codecContext, (uint8_t*)outbf, yuv_buffer_size, p_instanse->picture);
		//printf("+++++pic_in.i_csp=%d,pic_in.i_plane=%d,pic_in.i_stride[0]=%d,pic_in.i_stride[1]=%d,pic_in.i_stride[2]=%d,pic_in.i_stride[3]=%d++++++\n",pic_in.img.i_csp,pic_in.img.i_plane,pic_in.img.i_stride[0],pic_in.img.i_stride[1],pic_in.img.i_stride[2],pic_in.img.i_stride[3]);
	
		if(out_size <=0)
		{
			printf("---encode frame failed %d \n",gobale_FrameCount);
			return -5;
		}
		printf("---encode frame succeess size=%d  count=%d \n",out_size,gobale_FrameCount);
		//printf("+++++pic_out.i_csp=%d,pic_out.i_plane=%d,pic_out.i_stride[0]=%d,pic_out.i_stride[1]=%d,pic_out.i_stride[2]=%d,pic_out.i_stride[3]=%d++++++\n",pic_out.img.i_csp,pic_out.img.i_plane,pic_out.img.i_stride[0],pic_out.img.i_stride[1],pic_out.img.i_stride[2],pic_out.img.i_stride[3]);
		//printf("++++++pic_out.i_type=%d+++++nnal=%d++++++++nals->i_payload=%d+++++++++++\n",pic_out.i_type,nnal,nals->i_payload);

		all_size = out_size;

		int padding = 512 - all_size;
		if(padding < 0)
			padding = 0;

		p_instanse->real_count_fps++;
		
		if(*output_video_sample_size < all_size + padding)
		{		 
			fprintf(stderr ,"libvideoencode: Error output_video_sample_size is too small..\n");
			return -6;	  
		}

		memcpy(output_video_sample,outbf,all_size);
		memset(output_video_sample + all_size,0,padding);
		*output_video_sample_size = all_size + padding;
	}
	catch(...)
	{
		fprintf(stderr ,"catch: Error ..\n");
		return -7;	  
	}
	return 0;
}

int encode_video_sample_inc(video_encoder_t *videoencoder,const char *input_video_sample,int input_video_sample_size,
	char *output_video_sample,int *output_video_sample_size,int x,int y,int w,int h)
{
/*	if(videoencoder == NULL || input_video_sample == NULL || output_video_sample == NULL || output_video_sample_size == NULL)
	{        
		fprintf(stderr ,"libvideoencoder: Error paraments..\n");        
		return -1;    
	}
	
	videoencoder_instanse *p_instanse = (videoencoder_instanse *)videoencoder;


	int ret = -1;
	int all_size = 0;
	x264_picture_t pic_in;//yuv420p_picture;
	x264_picture_t pic_out;
	x264_nal_t *nals = NULL;
	int nnal = 0;

	int yuv_part_buffer_size = p_instanse->yuv_part_buffer_size;
	char *yuv_part_buffer = (char *)p_instanse->yuv_part_buffer;

	ret = BGRA_to_YUV420SP(w,h,input_video_sample,input_video_sample_size,yuv_part_buffer,&yuv_part_buffer_size);
	if(ret < 0)
	{
		fprintf(stderr ,"libvideoencoder: BGRA_to_YUV420SP error ret=%d..\n",ret);
		return -4;
	}

	//copy
	int yuv_buffer_size = p_instanse->yuv_buffer_size;
	char *yuv_buffer = (char *)p_instanse->yuv_buffer;
	for(int i = 0;i <h;i++)
	{
		memcpy(yuv_buffer	+  p_instanse->width*(y + i)  + x,yuv_part_buffer + i * w,w);
		if(i%2 == 0)
			memcpy(yuv_buffer+p_instanse->width*p_instanse->height+p_instanse->width*(y + i)/2+ x,yuv_part_buffer+w*h+w*i/2,w);
	}

	

	x264_picture_init(&pic_in);
	x264_picture_init(&pic_out);
	//printf("+++++pic_in.i_csp=%d,pic_in.i_plane=%d,pic_in.i_stride[0]=%d,pic_in.i_stride[1]=%d,pic_in.i_stride[2]=%d,pic_in.i_stride[3]=%d++++++\n",pic_in.img.i_csp,pic_in.img.i_plane,pic_in.img.i_stride[0],pic_in.img.i_stride[1],pic_in.img.i_stride[2],pic_in.img.i_stride[3]);

	pic_in.img.i_csp=p_instanse->colorspace;
	pic_in.img.i_plane=2;
	pic_in.img.i_stride[0]=p_instanse->width;
	pic_in.img.i_stride[1]=p_instanse->width;
	pic_in.img.plane[0] = (uint8_t *)yuv_buffer;
	pic_in.img.plane[1] = (uint8_t *)yuv_buffer + p_instanse->width*p_instanse->height;

	pic_in.i_pts = p_instanse->i_pts++;
	ret = x264_encoder_encode(p_instanse->x264_encoder, &nals, &nnal, &pic_in, &pic_out);
	if(ret < 0)
	{
		fprintf(stderr ,"libvideoencoder: x264_encoder_encode error ret=%d..\n",ret);
		//x264_picture_clean(&pic_out);
		return -5;
	}
	
	//printf("+++++pic_out.i_csp=%d,pic_out.i_plane=%d,pic_out.i_stride[0]=%d,pic_out.i_stride[1]=%d,pic_out.i_stride[2]=%d,pic_out.i_stride[3]=%d++++++\n",pic_out.img.i_csp,pic_out.img.i_plane,pic_out.img.i_stride[0],pic_out.img.i_stride[1],pic_out.img.i_stride[2],pic_out.img.i_stride[3]);
	//printf("++++++pic_out.i_type=%d+++++nnal=%d++++++++nals->i_payload=%d+++++++++++\n",pic_out.i_type,nnal,nals->i_payload);

	all_size = ret;
	
	int padding = 512 - all_size;
	if(padding < 0)
		padding = 0;

	p_instanse->real_count_fps++;
	
	if(*output_video_sample_size < all_size + padding)
	{		 
		fprintf(stderr ,"libvideosource: Error output_video_sample_size is too small..\n");
		//x264_picture_clean(&pic_out);
		return -6;	  
	}

	memcpy(output_video_sample,nals->p_payload,all_size);
	memset(output_video_sample + all_size,0,padding);
	*output_video_sample_size = all_size + padding;
	//x264_picture_clean(&pic_out);
*/	
	return 0;
}

unsigned long get_real_count_fps(video_encoder_t *videoencoder)
{
	if(videoencoder == NULL)
	{		 
		fprintf(stderr ,"libvideoencoder: Error paraments..\n");		
		return -1;	  
	}
	videoencoder_instanse *p_instanse = (videoencoder_instanse *)videoencoder;
	return p_instanse->real_count_fps;
}

int uninit_video_encoder(video_encoder_t *videoencoder)
{
	if(videoencoder == NULL)
	{        
		fprintf(stderr ,"libaudioencoder: Error paraments..\n");        
		return -1;    
	}
	videoencoder_instanse *p_instanse = (videoencoder_instanse *)videoencoder;

	free(p_instanse->yuv_buffer);
	free(p_instanse->yuv_part_buffer);
	avcodec_close(p_instanse->codecContext);
    av_free(p_instanse->codecContext);
    av_free(p_instanse->picture->data[0]);
    av_free(p_instanse->picture);

	free(p_instanse);

	return 0;
}


int main(int argc,char** argv)
{
	if(argc < 4)
		{
			printf("erro param \n");
			return -1;
		}
	int width = atoi(argv[2]);
	int height = atoi(argv[3]);
		printf("file path %s ,w =%d,h=%d \n",argv[1],width,height);

	int gopsize_min = 15;
	int gopsize_max = 15;
	int encode_bitrate=4500000;
	int fps = 25;

	InputParams_videoencoder video_encoder_params;
	
	video_encoder_params.width = width;
	video_encoder_params.height = height;
	video_encoder_params.gopsize_min = gopsize_min;
	video_encoder_params.gopsize_max = gopsize_max;
	video_encoder_params.bitrate = encode_bitrate;
	video_encoder_params.fps = fps;

	p_video_encoder = init_video_encoder(&video_encoder_params);
	if(p_video_encoder == NULL)
	{		 
		fprintf(stderr ,"avencoder: init_video_encoder fail..\n");		 
		return -1;	  
	}
	printf("file path %s ,w =%d,h=%d \n",argv[1],width,height);
	p_video_source_sample = (char *)malloc(n_video_source_sample_size);
	if( p_video_source_sample == NULL)
	{		 
		fprintf(stderr ,"avencoder: malloc fail..\n");		 
		return -2;	  
	}

	p_video_encode_sample = (char *)malloc(n_video_encode_sample_size);
	if( p_video_encode_sample == NULL)
	{		 
		fprintf(stderr ,"avencoder: malloc fail..\n");		 
		return -2;	  
	}
	
	FILE *fp = fopen(argv[1],"rb");
	FILE* fpout = fopen("out.es","wb+");
	if(fp==NULL)
	{
		printf("---file open failed \n");
		return -7;
	}
	printf("---beging read data \n");
	//循环读取数据
	while(1)
	{
		n_video_source_sample =fread(p_video_source_sample,1,width*height*3/2,fp);
		
			//编码视频
		printf("====read dat len =%d \n",n_video_source_sample);
		if(n_video_source_sample <= 0)
			break;
		n_video_encode_sample = n_video_encode_sample_size;
		//ret = encode_video_sample_inc(p_video_encoder,p_video_source_sample,n_video_source_sample,p_video_encode_sample,&n_video_encode_sample,x,y,w,h);
		int ret = encode_video_sample(p_video_encoder,(unsigned char*)p_video_source_sample,n_video_source_sample,p_video_encode_sample,&n_video_encode_sample);
		
		if(ret < 0)
		{		 
			fprintf(stderr ,"avencoder: encode_video_sample fail..\n");		 
			continue;	  
		}
		fwrite(p_video_encode_sample,1,n_video_encode_sample,fpout);
	}
	printf("---over\n");
	return 0;
}
