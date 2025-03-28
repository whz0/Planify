package com.chilltime.planifyapi.service;

import com.chilltime.planifyapi.entity.Codigo;
import com.chilltime.planifyapi.repository.CodigoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SACodigo {

    @Autowired
    private CodigoRepository codigoRepository;

    private static final int CODE_LENGTH = 6;
    private static final String CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Transactional
    public Codigo createCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CODE_CHARACTERS.length());
            sb.append(CODE_CHARACTERS.charAt(index));
        }

        Codigo codigo = new Codigo();
        codigo.setCodigo(sb.toString());
        codigoRepository.save(codigo);

        return codigo;
    }
}
