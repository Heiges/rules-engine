import { Tile } from '../components/Tile'
import './HomeView.css'

const tiles = [
  {
    id: 'attributes',
    name: 'Attribute',
    description: 'Verwalte die grundlegenden Attribute für Charaktere und Wesen in deiner Spielwelt.',
  },
]

export function HomeView() {
  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      <div className="tile-grid">
        {tiles.map((tile) => (
          <Tile key={tile.id} {...tile} />
        ))}
      </div>
    </div>
  )
}
