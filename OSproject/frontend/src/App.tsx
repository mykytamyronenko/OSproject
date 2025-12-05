import { Explorer } from "./features/explorer/Explorer";

function App() {
    return (
        <div className="h-screen w-screen bg-blue-100">
            <Explorer folders={[]} files={[]} onDeleteFolder={function(_id: number): void {
                throw new Error("Function not implemented.");
            } } onDeleteFile={function(_id: number): void {
                throw new Error("Function not implemented.");
            } } />
        </div>
    );
}

export default App;

