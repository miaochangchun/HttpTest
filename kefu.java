public String doPostQuery(String url,String query) throws HttpException{
		
		String result = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");					
		method.setRequestHeader("Content-type", "application/json;");
		client.getHttpConnectionManager().getParams().setConnectionTimeout(300000); 
		method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 300000);  
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(query.getBytes("UTF-8"),"UTF-8");
			method.setRequestEntity(requestEntity);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		}
				
		//发出请求
	    int stateCode = 0;
	    StopWatch stopWatch = new StopWatch();
		try {
			stopWatch.start();
			stateCode = client.executeMethod(method);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage());
		}finally{
			stopWatch.stop();
			logger.debug(stopWatch.toString()+"----|----"+query);
			if(stateCode==HttpStatus.SC_OK) { 
				try {
					result = method.getResponseBodyAsString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
			}else{				
				logger.error(query+"----|----返回状态码："+stateCode);
			}
			method.abort();
			method.releaseConnection();
			((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
		}
		return result;
	}
			