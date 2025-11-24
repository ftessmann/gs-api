## CEPIL TECHNOLOGIES

### Sistema de monitoramento ambiental e segurança para indústrias pesadas

O projeto consiste em uma solução de backend desenvolvida para monitorar, em tempo real, as condições ambientais de 
setores industriais de alto risco. O sistema processa dados telemétricos capturados por dispositivos IoT (ESP32) que 
monitoram variáveis críticas: temperatura, umidade, monóxido de carbono (CO), dióxido de carbono (CO2), metano (CH4) e 
gás sulfídrico (H2S).

#### Coleta IOT
Sensores gerenciados por ESP32 capturam variáveis críticas

#### Backend de processamento
Uma API REST recebe e persiste essas leituras, executando lógica de verificação imediata de limites de segurança

#### Interface móvel
Um app permite que gestores recebam alertas instantâneos e visualizem o status dos setores remotamente

O principal objetivo é garantir a segurança ocupacional e a integridade dos ativos industriais. A aplicação recebe as 
leituras dos sensores via API REST, persiste esses dados para auditoria histórica e executa uma lógica de verificação 
imediata de limites de segurança (ex: temperatura > 30°C ou CO2 > 800 ppm). Caso os parâmetros excedam os limiares 
pré-configurados, o sistema gera notificações automáticas de alerta para os supervisores e gestores responsáveis pelo 
setor afetado, permitindo intervenções rápidas para mitigar riscos de acidentes ou contaminação.

### Ferramentas de desenvolvimento

Para a construção deste projeto, foram selecionadas ferramentas padrão de mercado que garantem robustez, 
escalabilidade e manutenibilidade.

#### Java - Spring Boot

O Java é amplamente adotado em ambientes corporativos devido à sua tipagem estática forte e estabilidade. O Spring Boot 
foi escolhido por fornecer um modelo de configuração simplificada (convention over configuration) e um servidor embutido 
(Tomcat), permitindo o desenvolvimento ágil de APIs RESTful prontas para produção.

#### Expo - React
O Expo foi escolhido como a plataforma de desenvolvimento móvel por permitir a criação de aplicativos nativos para 
Android e iOS a partir de uma base de código única em JavaScript/TypeScript. Ele abstrai a complexidade de configuração 
do ambiente nativo (como Android Studio e Xcode), acelerando o ciclo de desenvolvimento e facilitando o teste em 
dispositivos físicos via Expo Go. Além disso, oferece bibliotecas nativas pré-configuradas para funcionalidades 
essenciais do projeto, como notificações

#### Docker

O Docker é utilizado para garantir a consistência do ambiente entre desenvolvimento e produção, eliminando o problema de 
"funciona na minha máquina". O docker-compose orquestra os serviços da aplicação (API) e do banco de dados (Postgres), 
definindo redes e volumes de forma declarativa, o que padroniza a infraestrutura necessária para rodar o projeto.

#### Postgres

O PostgreSQL é um Sistema Gerenciador de Banco de Dados Relacional (SGBDR) robusto, projetado para persistência segura 
em disco. Em um sistema de segurança industrial, o histórico de leituras de sensores é crítico para auditorias futuras 
e análise de acidentes, não podendo ser volátil. Implementa um controle de concorrência, permitindo que o sistema suporte
alto volume de escritas simultâneas, como as leituras dos sensores, sem bloquear leituras de relatórios pelos gestores.

#### Versões de produção

Imagens Alpine, baseadas no Alpine Linux, distro Linux extremamente leve e focada em eficiência de recursos, uma imagem 
Alpine tende a ser muitas vezes menor que uma imagem Debian ou Ubuntu, resultando em downloads mais rápidos, menor consumo
de armazenamento em disco e tempo de build e deploy reduzidos para pipelines de CI/CD.