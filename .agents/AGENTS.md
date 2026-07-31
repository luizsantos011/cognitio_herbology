# General Behaviors

## Commit Messages
- Mantenha as mensagens de commit naturais, simples e diretas (não robóticas).
- Evite especificidade excessiva. Não descreva detalhes técnicos minuciosos (ex: evite "corrigir erro de sintaxe no modelo JSON para o NeoForge", prefira "corrigir sintaxe no modelo").
- O foco deve ser comunicar o "que" foi feito de forma orgânica, como um humano faria, sem soar como uma máquina listando arquivos ou frameworks.
- Mantenha a atomicidade dos commits.
- NUNCA faça um `git commit` sozinho. Apenas implemente e SEMPRE espere pelo momento em que o usuário disser explicitamente que está pronto para commitar.
- Sempre lembre de commitar de maneira atômica, mas que faça sentido dentro do contexto das implementações.
- As mensagens de commit não devem ser específicas demais; devem ser claras o suficiente apenas para indicar O QUE deve ser feito e ONDE deve ser feito.
- SÓ FAÇA O PUSH quando tiver absoluta certeza de que o build está rodando perfeitamente (através de testes/compilação prévios), para evitar erros no deploy.
