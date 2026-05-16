INSERT INTO optimization_rules (id, name, description, rule_type, is_active) VALUES
(gen_random_uuid(),
 'Cortar gastos com entretenimento não-essenciais',
 'Gastos com entretenimento acima de 15% da renda mensal devem ser reduzidos em pelo menos 30%, pois raramente são essenciais e têm alto potencial de corte.',
 'EXPENSE_CUT', TRUE),

(gen_random_uuid(),
 'Priorizar renda fixa para perfis conservadores',
 'Investidores com tolerância a risco LOW devem alocar pelo menos 60% em renda fixa (CDB, Tesouro Direto) para garantir segurança do capital.',
 'INVESTMENT_PRIORITY', TRUE),

(gen_random_uuid(),
 'Fundo de emergência antes de investimentos',
 'Antes de qualquer investimento, o usuário deve ter reserva de emergência equivalente a 6 meses de despesas essenciais. Priorizar poupança/CDB liquidez diária.',
 'INVESTMENT_PRIORITY', TRUE),

(gen_random_uuid(),
 'Eliminar dívidas de alto custo primeiro',
 'Dívidas com juros acima de 2% ao mês (cartão de crédito, cheque especial) têm prioridade máxima. O ROI de quitar essas dívidas é superior a qualquer investimento.',
 'EXPENSE_CUT', TRUE),

(gen_random_uuid(),
 'Regra 50-30-20 de orçamento',
 'Distribuição ideal: 50% para necessidades essenciais (moradia, alimentação, saúde), 30% para desejos (entretenimento, lazer), 20% para poupança e investimentos.',
 'RISK_FILTER', TRUE),

(gen_random_uuid(),
 'Diversificação para perfis moderados',
 'Investidores MEDIUM risk devem diversificar: 40% renda fixa, 30% ações/fundos, 20% fundos imobiliários e 10% em outros ativos para balancear risco e retorno.',
 'INVESTMENT_PRIORITY', TRUE),

(gen_random_uuid(),
 'Maximização de ROI em perfis arrojados',
 'Investidores HIGH risk podem alocar até 60% em renda variável (ações, ETFs, cripto) buscando retornos acima de 12% ao ano, aceitando maior volatilidade.',
 'INVESTMENT_PRIORITY', TRUE),

(gen_random_uuid(),
 'Redução de gastos supérfluos com transporte',
 'Se o gasto com transporte supera 10% da renda, avaliar alternativas: transporte público, carpooling ou mudança de rotina podem gerar economia de 20-40%.',
 'EXPENSE_CUT', TRUE);

