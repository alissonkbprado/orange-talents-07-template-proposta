package br.com.zup_academy.alisson_prado.proposta.features.bloqueio_cartao.service;

import br.com.zup_academy.alisson_prado.proposta.model.*;
import br.com.zup_academy.alisson_prado.proposta.repository.BloqueiaCartaoRepository;
import br.com.zup_academy.alisson_prado.proposta.repository.CartaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class BloqueiaCartaoScheduler {

    private CartaoRepository cartaoRepository;
    private BloqueiaCartaoRepository bloqueiaCartaoRepository;
    private BloqueiaCartaoClientFeign clientFeign;
    private PlatformTransactionManager transactionManager;
    private final Logger logger = LoggerFactory.getLogger(Proposta.class);

    public BloqueiaCartaoScheduler(CartaoRepository cartaoRepository,
                                   BloqueiaCartaoClientFeign bloqueiaCartaoClientFeign,
                                   BloqueiaCartaoRepository bloqueiaCartaoRepository, PlatformTransactionManager transactionManager) {
        this.cartaoRepository = cartaoRepository;
        this.clientFeign = bloqueiaCartaoClientFeign;
        this.bloqueiaCartaoRepository = bloqueiaCartaoRepository;
        this.transactionManager = transactionManager;
    }

    /**
     * Rotina criada caso tenha ocorrido falha ao tentar processar o bloqueio do cartão com a API de Cartões no momento da solicitação do cliente.
     *
     * Rotina que consulta API externa de Cartões para bloquear cartão que estiver com StatusCartao.AGUARDANDO_BLOQUEIO
     *
     * Dados do Bloqueio persistidos na entidade Bloqueio.
     * O StatusCartao é atualizado para StatusCartao.BLOQUEADO.
     */
    @Scheduled(initialDelay = 18000, fixedDelayString = "${periodicidade.bloqueia-cartao}")
    protected void bloqueiaCartao(){

        List<Cartao> cartaoList = cartaoRepository.findFirst100ByStatus(StatusCartao.AGUARDANDO_BLOQUEIO);

        if(!cartaoList.isEmpty())
            cartaoList.forEach(cartao -> {
                if(cartao.bloqueia(clientFeign)) {
                    Bloqueio bloqueio = new Bloqueio(getIpServidor(), "(API) FeignClient bloqueia-cartao", StatusBloqueio.SUCESSO, cartao);
                    persiste(cartao, bloqueio);
                } else {
                    //Não foi possível acessar a API de cartões.
                    logger.error("Não foi possível bloquear o cartão devido a falha de comunicação com a API de cartões.");
                }
            });
    }

    private void persiste(Cartao cartao, Bloqueio bloqueio) {
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            cartaoRepository.save(cartao);
            bloqueiaCartaoRepository.save(bloqueio);
            transactionManager.commit(transactionStatus);
        } catch (RuntimeException ex){
            transactionManager.rollback(transactionStatus);
            throw ex;
        }
    }

    private String getIpServidor(){
        try {
            System.out.println("${local.server.port}");
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return InetAddress.getLoopbackAddress().getHostAddress();
        }
    }
}
