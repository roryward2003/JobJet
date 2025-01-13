package ie.tcd.scss.groupProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.tcd.scss.groupProject.domain.VerifToken;
import ie.tcd.scss.groupProject.repo.VerifTokenRepository;

@Service
public class VerificationService {
    private final VerifTokenRepository verifTokenRepository;

    @Autowired
    public VerificationService(VerifTokenRepository verifTokenRepository){
        this.verifTokenRepository = verifTokenRepository;
    }

    public VerifToken getVerifToken(String token) {
        return verifTokenRepository.findByToken(token);
    }
}
