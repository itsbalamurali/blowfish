package com.avantir.blowfish.consumers.rest.api.mgt;

import com.avantir.blowfish.model.AcquirerBin;
import com.avantir.blowfish.model.BlowfishLog;
import com.avantir.blowfish.model.MerchantBin;
import com.avantir.blowfish.services.AcquirerBinService;
import com.avantir.blowfish.services.AcquirerService;
import com.avantir.blowfish.services.MerchantBinService;
import com.avantir.blowfish.services.MerchantService;
import com.avantir.blowfish.utils.BlowfishUtil;
import com.avantir.blowfish.utils.IsoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lekanomotayo on 18/02/2018.
 */
@RestController
@RequestMapping(value = "api/v1/merchants/bins", produces = "application/hal+json")
public class MerchantBinController {


    private static final Logger logger = LoggerFactory.getLogger(MerchantBinController.class);
    @Autowired
    MerchantService merchantService;
    @Autowired
    MerchantBinService acquirerBinService;


    @RequestMapping(method= RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public Object create(@RequestBody MerchantBin acquirerBin, HttpServletResponse response)
    {
        try{
            acquirerBinService.create(acquirerBin);
            response.setStatus(HttpServletResponse.SC_CREATED);
            acquirerBin = getLinks(acquirerBin, response);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.PATCH, consumes = "application/json", value = "/{id}")
    @ResponseBody
    public Object update(@PathVariable("id") long id, @RequestBody MerchantBin newAcquirerBin, HttpServletResponse response)
    {
        try{
            if(newAcquirerBin == null)
                throw new Exception();

            newAcquirerBin.setMerchantBinId(id);
            newAcquirerBin = acquirerBinService.update(newAcquirerBin);
            response.setStatus(HttpServletResponse.SC_OK);
            newAcquirerBin = getLinks(newAcquirerBin, response);
            return newAcquirerBin;
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, consumes = "application/json", value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object delete(@PathVariable("id") long id, HttpServletResponse response)
    {
        try{
            acquirerBinService.delete(id);
            response.setStatus(HttpServletResponse.SC_OK);
            return "";
        }
        catch(Exception ex){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }

    @RequestMapping(method= RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public Object get(@RequestHeader(value="id", required = false) Long id, @RequestHeader(value="merchantId", required = false) Long merchantId, @RequestHeader(value="binId", required = false) Long binId, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ", merchantId=" + merchantId + ", binId=" + binId + ",HttpServletResponse=" + response.toString();
        try
        {
            if(id != null && id > 0)
                return getById(id, response);


            if(merchantId != null && merchantId > 0)
                return getByMerchantId(merchantId, response);

            if(binId != null && binId > 0)
                return getByBinId(binId, response);

            List<MerchantBin> merchantBinList = acquirerBinService.findAll();
            response.setStatus(HttpServletResponse.SC_OK);
            for (MerchantBin merchantBin : merchantBinList) {
                merchantBin = getLinks(merchantBin, response);
            }

            return merchantBinList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    @RequestMapping(method= RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public Object getById(@PathVariable Long id, HttpServletResponse response)
    {
        String fxnParams = "id=" + id + ",HttpServletResponse=" + response.toString();
        try
        {
            MerchantBin merchantBin = acquirerBinService.findByMerchantBinId(id);
            response.setStatus(HttpServletResponse.SC_OK);
            merchantBin = getLinks(merchantBin, response);
            return merchantBin;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }



    public Object getByMerchantId(Long merchantId, HttpServletResponse response)
    {
        String fxnParams = "merchantId=" + merchantId + ",HttpServletResponse=" + response.toString();
        try
        {
            List<MerchantBin> merchantBinList = acquirerBinService.findByMerchantId(merchantId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (MerchantBin merchantBin : merchantBinList) {
                merchantBin = getLinks(merchantBin, response);
            }
            return merchantBinList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }


    public Object getByBinId(Long binId, HttpServletResponse response)
    {
        String fxnParams = "binId=" + binId + ",HttpServletResponse=" + response.toString();
        try
        {
            List<MerchantBin> merchantBinList = acquirerBinService.findByBinId(binId);
            response.setStatus(HttpServletResponse.SC_OK);
            for (MerchantBin merchantBin : merchantBinList) {
                merchantBin = getLinks(merchantBin, response);
            }
            return merchantBinList;
        }
        catch(Exception ex)
        {
            BlowfishLog log = new BlowfishLog(fxnParams, ex);
            logger.error(log.toString());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return BlowfishUtil.getError(IsoUtil.RESP_06, ex.getMessage());
        }
    }



    private MerchantBin getLinks(MerchantBin merchantBin, HttpServletResponse response){
        Link selfLink = ControllerLinkBuilder.linkTo(MerchantBinController.class).slash(merchantBin.getMerchantBinId()).withSelfRel();
        merchantBin.add(selfLink);

        Object linkBuilder2 = ControllerLinkBuilder.methodOn(MerchantController.class).getById(merchantBin.getMerchantId(), response);
        Link link2 = ControllerLinkBuilder.linkTo(linkBuilder2).withRel("merchant");
        merchantBin.add(link2);

        Object linkBuilder1 = ControllerLinkBuilder.methodOn(BinController.class).getById(merchantBin.getBinId(), response);
        Link link1 = ControllerLinkBuilder.linkTo(linkBuilder1).withRel("bin");
        merchantBin.add(link1);

        return merchantBin;
    }

}