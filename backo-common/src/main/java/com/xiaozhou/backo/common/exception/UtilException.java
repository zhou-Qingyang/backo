package com.xiaozhou.backo.common.exception;

/**   
 * @Description:   TODO(这里用一句话描述这个类的作用)   
 * @date:          2023年2月9日 下午4:32:25     
 */
public class UtilException extends RuntimeException {
	
	   private static final long serialVersionUID = 8247610319171014183L;

	    public UtilException(Throwable e)
	    {
	        super(e.getMessage(), e);
	    }

	    public UtilException(String message)
	    {
	        super(message);
	    }

	    public UtilException(String message, Throwable throwable)
	    {
	        super(message, throwable);
	    }

}
