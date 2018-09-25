package com.example.demo;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionGetController {
	
	CalculationTransaction ct=new CalculationTransaction();
	static int count=0,avg=0;
	static float sum=0;
	static float min=9999,max=0;
	
	@RequestMapping(value="/statistics",consumes= MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE, method =RequestMethod.POST )
	public ResponseEntity<List<AmountTransaction>> TransactionMethod(@RequestBody List<AmountTransaction> transact) throws ParseException
	{
		
		long currenttime=System.currentTimeMillis();
		
		/*long usertime=0;
		if((currenttime-usertime)<60000)
			return new ResponseEntity<List<AmountTransaction>>(transact, HttpStatus.CREATED);
		else
			return new ResponseEntity<List<AmountTransaction>>(transact, HttpStatus.NO_CONTENT) ;*/
		
		transact.stream().forEach(c -> 
			{
				long usertime=c.getTimestamp();
				if((currenttime-usertime)<60000)
				{
					count=count+1;
					sum=sum+c.getAmount();
					if(min>c.getAmount())
						min=c.getAmount();
					if(max<c.getAmount())
						max=c.getAmount();
					System.out.println(currenttime-usertime);
					System.out.println(ct.getCount());
				} 
			});
		if(count==0)
			avg=0;
		else
		{
			avg=(int)sum/count;
		}
		System.out.println(ct.getCount());
		return new ResponseEntity<List<AmountTransaction>>( HttpStatus.CREATED);
	}

	@ResponseBody
	@RequestMapping(value="/statistics1", method =RequestMethod.GET )
	public ResponseEntity<CalculationTransaction> TransactiongetMethod(CalculationTransaction transact)
	{
		transact.setCount(count);
		transact.setTotal(sum);
		transact.setMax((int)max);
		transact.setMin((int)min);
		transact.setAverage(avg);
		return new ResponseEntity<CalculationTransaction>(transact, HttpStatus.OK); 
	}
	
}
