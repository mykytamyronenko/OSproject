package project.aemtaugust1.Application.Utils;

public interface ICommandsHandler<I,O> {
    O handle(I input);
}
