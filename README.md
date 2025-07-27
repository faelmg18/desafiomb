
# Desafio Mercantil do Brasil

Esse projeto tem como objetivo mostrar a arquitetura criada para o desafio pedido pelo Mercantil do Brasil.
Obs: Alguns commits estão com nome de Ahttpe pois fiz no perfil do meu mac onde tenho meu cnpj com esse nome.

## 🎯 Objetivo

* Criar um aplicativo Android nativo em Kotlin com XML, que utilize boas práticas de desenvolvimento, incluindo:
1. Geração de UUID Personalizado
2. Login com Biometria
3. Animação estilo "DVD Screensaver"
4. Integração com API REST (com mock) e sem mock
5. Componente PopOver de Data e Hora

# Estrutura do Projeto
O projeto está organizado em módulos que seguem o padrão MVVM com Clean Architecture, utilizando tecnologias como Koin para injeção de dependência, Kotlin, Coroutines, Retrofit, e SharedPreferences criptografado para segurança.

1. auth
   
  * Contém a lógica relacionada à autenticação do usuário.
  
  * Exemplo: BiometricLoginManager que gerencia o login via biometria (impressão digital ou face).
  
  * Responsável por garantir que o usuário seja autenticado usando métodos seguros.

2. core
     
  * Contém funcionalidades e utilitários reutilizáveis em todo o projeto.
  
  * CacheProvider: Interface para armazenamento seguro e acesso a dados em cache (exemplo: SharedPreferences criptografado).
  
  * DeviceUtil: Funções utilitárias relacionadas ao dispositivo, como coleta de informações dos sensores e hardware.
  
  * Providers: Classes que fornecem dependências ou serviços genéricos.
  
  * MercantilUUID: Implementação para gerar um identificador único do dispositivo baseado em sensores e hardware.

3. shared
     
  * Armazena dados e lógicas compartilhadas entre diferentes features.
  
  * LoggedInUser: Representa o usuário atualmente autenticado.
  
  * UserRepository: Responsável por acessar e manipular dados do usuário localmente.

  ## Tecnologias e padrões utilizados
MVVM (Model-View-ViewModel): Para separar responsabilidades entre UI, lógica e dados, facilitando testes e manutenção.

Clean Architecture: Organização do código em camadas claras (core, auth, feature, shared) para maior modularidade e independência.

Koin: Biblioteca para injeção de dependência, que facilita a criação e o gerenciamento dos objetos do projeto.

Kotlin e Coroutines: Para programação assíncrona.

Retrofit: Cliente HTTP para comunicação com APIs REST.

SharedPreferences criptografado: Para armazenamento seguro de dados sensíveis, como UUIDs entre outros.

## Aqui irei mostrar algumas classes importantes.

# BiometricLoginManager
Essa classe gerencia o login usando biometria (impressão digital ou reconhecimento facial) no app Android.

## Como funciona
* Recebe o contexto do app e um gerador de UUID (um identificador único).

* Quando o método authenticate é chamado, abre o prompt de autenticação biométrica.

* Se o usuário autenticar com sucesso, retorna um UUID gerado Pela classe `MercantilUUID` (simbolizando o login válido).

* Se ocorrer erro ou falha na autenticação, retorna uma mensagem de erro para ser tratada na UI.

4. feature

* Contém as funcionalidades específicas do app divididas em módulos independentes.

* home: Implementa a tela principal do aplicativo, com toda a lógica de UI, ViewModel e interação.

* login: Implementa a tela de login, incluindo a lógica para autenticação e fluxo de acesso.

### Detalhes importantes
* Usa o BiometricPrompt do AndroidX para mostrar a interface nativa de biometria.

* Exibe título, subtítulo e botão cancelar na janela de biometria.

* Executa as callbacks para sucesso e erro diretamente no thread principal.

## Como usar

```kotlin
// injetar o LoginManager via koin

private val loginManager: LoginManager by inject()

loginManager.authenticate(context, { userUUID ->
    onUserAuthenticated(userUUID)
}) {
    onErrorOnUserAuthenticateUser(it)
}
```

# MercantilUUID
Essa classe gera um identificador único (UUID) para o dispositivo usando informações dos sensores e hardware.

## Como funciona

* Verifica se já existe um UUID salvo no cache (SharedPreferences) para evitar gerar sempre.

* Se não existir, coleta informações do acelerômetro e do sensor de luz do dispositivo, junto com dados do hardware.

* Combina esses dados em uma string única e gera um hash SHA-256, que vira uma "impressão digital" do dispositivo.

* Salva esse UUID gerado no cache para reutilização futura.

* Retorna o UUID, que é uma string codificada em Base64.

## Por que usar?
* Garante um identificador único para o dispositivo.

* Reduz consumo de recursos ao armazenar o valor gerado.

* Usa sensores e hardware para aumentar a singularidade do ID.

## Como usar

```kotlin

// Basta injetar via koin a classe MercantilUUID
private val mercantilUUID : MercantilUUID by inject()

// Para obter o UUID único do dispositivo
private val deviceUUID: String = mercantilUUID.getUUID()

// Mostra a saida do identificador gerado
println("UUID do dispositivo: $deviceUUID")

```

## Demo

Video de demostração. Obs. A tela fica preta em alguns momentos, pois é aberta a biometria nativa do dispositivo e por questões de segurança essa tela não pode ser gravada nem captuada.

https://github.com/user-attachments/assets/a0940ee8-c1ee-46d0-b659-54acb580569a


## Authors

- [@Rafael Henrique](https://github.com/faelmg18/)
- [Meu Canal no Youtube](https://www.youtube.com/@RafaelDevAndroid)
- [Minha pagina no Medium](https://medium.com/@faelmg18android)


## Licenças

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)


