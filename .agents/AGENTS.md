# General Behaviors

## Commit Messages
- Mantenha as mensagens de commit naturais, simples e diretas (não robóticas).
- Evite especificidade excessiva. Não descreva detalhes técnicos minuciosos (ex: evite "corrigir erro de sintaxe no modelo JSON para o NeoForge", prefira "corrigir sintaxe no modelo").
- O foco deve ser comunicar o "que" foi feito de forma orgânica, como um humano faria, sem soar como uma máquina listando arquivos ou frameworks.
- Mantenha a atomicidade dos commits.
- NUNCA faça um `git commit` ou `git push` automaticamente após escrever/compilar um código. Sempre peça para o usuário testar in-game e confirmar se a funcionalidade ou interface visual está 100% correta. Apenas realize o commit DEPOIS que a validação for um sucesso absoluto.
