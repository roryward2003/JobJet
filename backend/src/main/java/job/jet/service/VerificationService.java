package job.jet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import job.jet.domain.VerifToken;
import job.jet.repo.VerifTokenRepository;

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
