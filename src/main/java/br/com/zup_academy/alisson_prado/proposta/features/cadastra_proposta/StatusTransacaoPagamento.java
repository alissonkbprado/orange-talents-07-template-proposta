package br.com.zup_academy.alisson_prado.proposta.features.cadastra_proposta;

import br.com.zup_academy.alisson_prado.proposta.model.StatusProposta;

public enum StatusTransacaoPagamento {
    SEM_RESTRICAO{
        @Override
        public StatusProposta getStatusTransacaoPagamento() {
            return StatusProposta.ELEGIVEL;
        }
    },
    COM_RESTRICAO {
        @Override
        public StatusProposta getStatusTransacaoPagamento() {
            return StatusProposta.NAO_ELEGIVEL;
        }
    },
    ;

    public abstract StatusProposta getStatusTransacaoPagamento();

}
