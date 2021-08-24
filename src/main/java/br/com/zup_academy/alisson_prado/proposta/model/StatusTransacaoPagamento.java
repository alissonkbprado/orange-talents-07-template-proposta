package br.com.zup_academy.alisson_prado.proposta.model;

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
