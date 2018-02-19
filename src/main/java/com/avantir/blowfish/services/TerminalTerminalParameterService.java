package com.avantir.blowfish.services;

/**
 * Created by lekanomotayo on 14/10/2017.
 */

import com.avantir.blowfish.model.MerchantTerminalParameter;
import com.avantir.blowfish.model.TerminalTerminalParameter;
import com.avantir.blowfish.repository.MerchantTerminalParameterRepository;
import com.avantir.blowfish.repository.TerminalTerminalParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer.
 * Specify transactional behavior and mainly
 * delegate calls to Repository.
 */
@Component
public class TerminalTerminalParameterService {

    public static final String ALL = "all";
    public static final String ACTIVE = "active";

    @Autowired
    private TerminalTerminalParameterRepository terminalTerminalParameterRepository;


    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public TerminalTerminalParameter findById(Long id) {

        try
        {
            //Optional<AcquirerMerchant> optional = acquirerMerchantRepository.findById(id);
            //return optional.orElse(null);
            return terminalTerminalParameterRepository.findById(id);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Cacheable(value = "termTermParam")
    @Transactional(readOnly=true)
    public TerminalTerminalParameter findByTerminalId(Long terminalId) {

        try
        {
            return terminalTerminalParameterRepository.findByTerminalId(terminalId);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }



}
