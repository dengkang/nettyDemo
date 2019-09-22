为什么需要netty
官方给出的理由：
Netty是一个NIO客户端服务器框架，可以快速轻松地开发网络应用程序，例如协议服务器和客户端。它极大地简化和简化了TCP和UDP套接字服务器等网络编程。

“快速简便”并不意味着最终的应用程序将遭受可维护性或性能问题的困扰。Netty经过精心设计，结合了许多协议（例如FTP，SMTP，HTTP以及各种基于二进制和文本的旧式协议）的实施经验。结果，Netty成功地找到了一种无需妥协即可轻松实现开发，性能，稳定性和灵活性的方法。

特征
设计
	适用于各种传输类型的统一API-阻塞和非阻塞套接字
	基于灵活且可扩展的事件模型，可将关注点明确分离
	高度可定制的线程模型-单线程，一个或多个线程池，例如SEDA
	真正的无连接数据报套接字支持（从3.1开始）
使用方便
	记录良好的Javadoc，用户指南和示例
	没有其他依赖关系，JDK 5（Netty 3.x）或6（Netty 4.x）就足够了
	注意：某些组件（例如HTTP / 2）可能有更多要求。请参阅 需求页面 以获取更多信息。
性能
	更高的吞吐量，更低的延迟
	减少资源消耗
	减少不必要的内存复制
安全
	完整的SSL / TLS和StartTLS支持
社区
	提前发布，经常发布


netty
netty4需要jdk6以上

DiscardServerHandler 对收到的消息不处理或者只打印，客户端通过telnet localhost port 进行连接发送消息
EchoServerHandler 将受到的消息发送客户端
TimeServerHandler 只发送一个时间信息给客户端，然后关闭连接

基于流传输面临的问题：
在基于流的传输（例如TCP / IP）中，将接收到的数据存储到套接字接收缓冲区中。
不幸的是，基于流的传输的缓冲区不是数据包队列而是字节队列。
这意味着，即使您将两个消息作为两个独立的数据包发送，操作系统也不会将它们视为两个消息，而只是一堆字节。
因此，不能保证您阅读的内容与您的远程对等方写的完全一样。
例如，让我们假设操作系统的TCP / IP堆栈已收到三个数据包：
ABC DEF GHI
由于基于流的协议具有此一般属性，因此很有可能在您的应用程序中以以下分段形式读取它们：
A BC DEFG HI
因此，无论是服务器端还是客户端，接收方都应将接收到的数据整理到一个或多个有意义的帧中，以使应用程序逻辑易于理解。在上面的示例中，接收到的数据应采用以下格式：
ABC DEF GHI

现在让我们回到TIME客户示例。我们在这里有同样的问题。32位整数是非常少量的数据，并且不太可能经常碎片化。但是，问题在于它可以被碎片化，并且碎片化的可能性会随着流量的增加而增加。
一种简单的解决方案是创建一个内部累积缓冲区，然后等待直到所有4个字节都被接收到内部缓冲区中为止
方案二 通过解码器 ,在socketChannel.pipeline().addLast(new TimeDecoder(),new TimeServerHandler()); 通过指定解码器，在解码器中做处理
此外，Netty提供了开箱即用的解码器，使您能够非常轻松地实现大多数协议，并避免最终以单一的，不可维护的处理程序实现而告终。
io.netty.example.factorial  for a binary protocol, and （https://netty.io/4.1/xref/io/netty/example/factorial/package-summary.html）
io.netty.example.telnet for a text line-based protocol. (https://netty.io/4.1/xref/io/netty/example/telnet/package-summary.html)

用POJO而不是ByteBuf
在ChannelHandlers中使用POJO的优势显而易见。 通过将从ByteBuf中提取信息的代码从处理程序中分离出来，您的处理程序将变得更加可维护和可重用。 在TIME客户端和服务器示例中，我们仅读取一个32位整数，并且直接使用ByteBuf并不是主要问题。 但是，您会发现在实现实际协议时有必要进行分离。
将TimeServerHandler中 channelActive，TimeClienthandler中channelRead 中设计到msg的用UnixTime代替ByteBuf
增加TimeEncoder/TimeEncoderSecond编码转换
